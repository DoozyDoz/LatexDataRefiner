package com.example.latexdatarefiner

import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.latexdatarefiner.databinding.ActivityDetailBinding
import java.io.File
import org.apache.commons.text.StringEscapeUtils
import android.util.Base64


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val latexData = intent.getParcelableExtra<LatexData>(EXTRA_LATEX_DATA)

        if (latexData != null) {
            val imageView: ImageView = findViewById(R.id.imageView)
            val latexTextView: TextView = findViewById(R.id.latexTextView)
            val latexWebView: WebView = findViewById(R.id.latexWebView)

            val imageUri = Uri.fromFile(File(latexData.imagePath))
            imageView.setImageURI(imageUri)
            latexTextView.text = latexData.latexCode

            val displayMathReplaced = latexData.latexCode.replace("$$", "")
            val inlineMathReplaced = displayMathReplaced.replace("$", "\\(")
            val correctedLatex = inlineMathReplaced.replace("\\(", "", false)
            val escapedLatex = StringEscapeUtils.escapeEcmaScript(correctedLatex)
            val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.15.1/dist/katex.min.css" crossorigin>
            </head>
            <body>
                <script src="https://cdn.jsdelivr.net/npm/katex@0.15.1/dist/katex.min.js" crossorigin></script>
                <div id="latex"></div>
                <script>
                    document.addEventListener('DOMContentLoaded', function() {
                        var latex = '$escapedLatex';
                        var el = document.getElementById('latex');
                        try {
                            katex.render(latex, el);
                        } catch (error) {
                            el.innerHTML = '<span style="color: red;">Error rendering LaTeX: ' + error.message + '</span>';
                        }
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

            latexWebView.settings.javaScriptEnabled = true
            latexWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    }

    companion object {
        const val EXTRA_LATEX_DATA = "com.example.app.EXTRA_LATEX_DATA"
    }
}
