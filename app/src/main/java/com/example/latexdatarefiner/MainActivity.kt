package com.example.latexdatarefiner

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.latexdatarefiner.databinding.ActivityMainBinding
import jxl.Workbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MainActivity : AppCompatActivity() {


    private val REQUEST_CODE_STORAGE_PERMISSIONS = 1001
    private lateinit var binding: ActivityMainBinding
    private lateinit var latexDataAdapter: LatexDataAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latexDataDao = AppDatabase.getInstance(this).latexDataDao()
        val latexDataList = latexDataDao.getAll()

        latexDataAdapter = LatexDataAdapter(mutableListOf()){
            LatexDetailsActivity.sQuestionId = it.id.toString()
            LatexDetailsActivity.sImage = it.imagePath
            val intent = Intent(this, LatexDetailsActivity::class.java)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = latexDataAdapter


        latexDataList.observe(this) { list ->
            latexDataAdapter.updateData(list)
        }

        val inputStream = assets.open("data.xls")
        val workbook = Workbook.getWorkbook(inputStream)

        val sheet = workbook.getSheet(0)
        val numRows = sheet.rows

        requestStoragePermissions {
            GlobalScope.launch {
                var downloadedImages = 0
                for (i in 0 until numRows) {
                    val row = sheet.getRow(i)
                    val latexCode = row[0].contents
                    val link = row[1].contents

                    val fileName = "image_${i}.png"
                    val imagePath = downloadImageAndSaveToStorage(link, fileName)

                    if (imagePath != null) {
                        val latexData =
                            LatexData(id = 0, latexCode = latexCode, imagePath = imagePath, katexCode = "")
                        latexDataDao.insert(latexData)
                    }
                    withContext(Dispatchers.Main) {
                        downloadedImages++
                        binding.progressBar.progress =
                            (downloadedImages.toFloat() / numRows.toFloat() * 100).toInt()
                    }
                }
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun downloadImageAndSaveToStorage(url: String, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File(dir, fileName)

                if (!file.exists()) {
                    val input = URL(url).openStream()
                    val output = FileOutputStream(file)
                    input.use { input ->
                        output.use { output ->
                            input.copyTo(output)
                        }
                    }
                }
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun requestStoragePermissions(onPermissionGranted: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE_PERMISSIONS
            )
        } else {
            // Permissions already granted, proceed with your app's functionality
            onPermissionGranted()
        }
    }

    private var onPermissionGranted: (() -> Unit)? = null


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with your app's functionality
                    onPermissionGranted?.invoke()
                } else {
                    // Permission denied, show a message or handle the denial as appropriate
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        onPermissionGranted = null
        super.onDestroy()
    }


}
