package com.example.latexdatarefiner

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.Call
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.example.latexdatarefiner.databinding.FragmentKatexBinding
import com.example.latexdatarefiner.databinding.FragmentLatexBinding
import katex.hourglass.`in`.mathlib.MathView
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors


class LatexFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentLatexBinding? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLatexBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val bitmap = BitmapFactory.decodeFile(LatexDetailsActivity.sImage!!)
        binding.livTex.setImageBitmap(bitmap)
    }

    companion object {
        fun newInstance(): LatexFragment {
            return LatexFragment()
        }
    }
}