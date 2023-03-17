package com.example.latexdatarefiner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.recyclerview.widget.LinearLayoutManager
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


    private lateinit var binding: ActivityMainBinding
    private lateinit var latexDataAdapter: LatexDataAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latexDataDao = AppDatabase.getInstance(this).latexDataDao()
        val latexDataList = latexDataDao.getAll()

        latexDataAdapter = LatexDataAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = latexDataAdapter


        latexDataList.observe(this) { list ->
            latexDataAdapter.updateData(list)
        }

        val inputStream = assets.open("data.xls")
        val workbook = Workbook.getWorkbook(inputStream)

        val sheet = workbook.getSheet(0)
        val numRows = sheet.rows

        GlobalScope.launch {
            for (i in 0 until numRows) {
                val row = sheet.getRow(i)
                val latexCode = row[0].contents
                val link = row[1].contents

                val fileName = "image_${i}.png"
                val imagePath = downloadImageAndSaveToStorage(link, fileName)

                if (imagePath != null) {
                    val latexData = LatexData(id = 0, latexCode = latexCode, imagePath = imagePath)
                    latexDataDao.insert(latexData)
                }
            }
        }
    }

    suspend fun downloadImageAndSaveToStorage(url: String, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File(dir, fileName)

                val input = URL(url).openStream()
                val output = FileOutputStream(file)
                input.use { input ->
                    output.use { output ->
                        input.copyTo(output)
                    }
                }

                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


}
