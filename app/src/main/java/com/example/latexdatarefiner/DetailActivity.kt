package com.example.latexdatarefiner

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.latexdatarefiner.databinding.ActivityDetailBinding
import com.example.latexdatarefiner.databinding.ActivityMainBinding
import java.io.File


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latexData = intent.getParcelableExtra<LatexData>(EXTRA_LATEX_DATA)

        if (latexData != null) {

            val imageUri = Uri.fromFile(File(latexData.imagePath))
            binding.imageView.setImageURI(imageUri)
            binding.latexTextView.text = latexData.latexCode
        }
    }

    companion object {
        const val EXTRA_LATEX_DATA = "com.example.app.EXTRA_LATEX_DATA"
    }
}
