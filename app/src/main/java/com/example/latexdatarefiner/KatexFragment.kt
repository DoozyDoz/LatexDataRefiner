package com.example.latexdatarefiner

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.latexdatarefiner.databinding.FragmentKatexBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors


class KatexFragment : Fragment(), View.OnClickListener {
    var mLatexData: LatexData? = null
    var mQuestionService: QuestionService? = null

    private val binding get() = _binding!!
    private var _binding: FragmentKatexBinding? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKatexBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val latexDataId =
            LatexDetailsActivity.sQuestionId // Replace this with the actual ID you want to fetch
        val dao = AppDatabase.getInstance(requireContext()).latexDataDao()

        mLatexData = runBlocking {
            dao.getLatexDataById(latexDataId!!.toInt())
        }

        mQuestionService = APIUtils.questionService
        if (mLatexData!!.katexCode.isNotEmpty()) {
            binding.tvKatex.setText(mLatexData!!.katexCode)
            binding.btnQuestion.isEnabled = true
            binding.btnAnswer.isEnabled = false
        } else {
            binding.tvKatex.setText(mLatexData!!.latexCode)
            binding.btnAnswer.isEnabled = false
        }


        binding.tvKatex.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            if (view.id == R.id.tv_katex) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> view.parent
                        .requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
        binding.btnRun.setOnClickListener {
            binding.kvAnswer.visibility = View.VISIBLE
            binding.bgImage.visibility = View.GONE
            render()
        }
        binding.btnText.setOnClickListener {
            val selectedText = selection
            if (!selectedText.isEmpty()) {
                //https://stackoverflow.com/a/20887690/8872691
                val inputString = binding.tvKatex.getText().toString()
                val modifiedString =
                    resources.getString(R.string.replace_text, decorateSelection(selectedText))
                val start = Math.max(binding.tvKatex.getSelectionStart(), 0)
                val end = Math.max(binding.tvKatex.getSelectionEnd(), 0)
                binding.tvKatex.getText().replace(
                    Math.min(start, end), Math.max(start, end),
                    modifiedString, 0, modifiedString.length
                )
            }
        }
        binding.btnFunction.setOnClickListener {
            val selectedText = selection
            if (selectedText.isNotEmpty()) {
                replaceSelection(R.string.replace_function, selectedText)
            }
        }
        binding.btnReturn.setOnClickListener { view: View ->
            onClick(
                view
            )
        }
        binding.btnIndent.setOnClickListener { view: View ->
            onClick(
                view
            )
        }
        binding.btnCenter.setOnClickListener { view: View ->
            onClick(
                view
            )
        }
        binding.btnBold.setOnClickListener { view: View ->
            onClick(
                view
            )
        }
        binding.btnQuestion.setOnClickListener { view: View ->
            onClick(
                view
            )
        }
        binding.btnAnswer.setOnClickListener { view: View ->
            onClick(
                view
            )
        }
    }

    private fun decorateSelection(selectedText: String): String? {
        val trimAndSplitUnicode =
            selectedText.trim { it <= ' ' }.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        var decorated: String? = null
        decorated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Arrays.stream(trimAndSplitUnicode)
                .collect(Collectors.joining("\\space ")) // https://stackoverflow.com/a/30220543/8872691
        } else {
            TextUtils.join(
                "\\space ",
                trimAndSplitUnicode
            ) // https://stackoverflow.com/a/33803041/8872691
        }
        return decorated
    }

    private fun replaceSelection(place_holder: Int, selectedText: String) {
        //https://stackoverflow.com/a/20887690/8872691
        val inputString = binding.tvKatex.text.toString()
        val res = resources
        val modifiedString = res.getString(place_holder, selectedText)
        val selectionModifiedString = inputString.replace(selectedText, modifiedString)
        binding.tvKatex.setText(selectionModifiedString)
    }

    private val selection: String
        get() {
            val startSelection = binding.tvKatex.selectionStart
            val endSelection = binding.tvKatex.selectionEnd
            return binding.tvKatex.text.toString().substring(startSelection, endSelection)
        }

    private fun replaceSelectionWith(textToInsert: String) {
        val start = binding.tvKatex.selectionStart.coerceAtLeast(0)
        val end = binding.tvKatex.selectionEnd.coerceAtLeast(0)
        binding.tvKatex.text.replace(
            start.coerceAtMost(end), start.coerceAtLeast(end),
            textToInsert, 0, textToInsert.length
        )
    }

    private fun render() {
        binding.kvAnswer.setDisplayText(binding.tvKatex.text.toString())
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_question -> {
                val updatedLatexData = mLatexData!!.copy(katexCode = binding.tvKatex.text.toString())
                updateQuestion(updatedLatexData)
            }

            R.id.btn_return -> replaceSelectionWith(resources.getString(R.string.replace_return))
            R.id.btn_indent -> replaceSelectionWith(resources.getString(R.string.replace_indent))
            R.id.btn_center -> {
                val selectedText = selection
                if (selectedText.isNotEmpty()) {
                    replaceSelection(R.string.replace_center, selectedText)
                }
            }
            R.id.btn_bold -> {
                val selectedText2 = selection
                if (selectedText2.isNotEmpty()) {
                    val inputString = binding.tvKatex.text.toString()
                    val modifiedString =
                        resources.getString(R.string.replace_bold, decorateSelection(selectedText2))
                    val selectionModifiedString = inputString.replace(selectedText2, modifiedString)
                    binding.tvKatex.setText(selectionModifiedString)
                }
            }
        }
    }

    fun updateQuestion(question: LatexData?) {
//        val call = mQuestionService!!.updateQuestion(id, question)
//        call?.enqueue(object : Callback<Question?> {
//            override fun onResponse(
//                call: retrofit2.Call<Question?>,
//                response: Response<Question?>
//            ) {
//                if (response.isSuccessful) {
//                    Toast.makeText(context, "Question updated successfully!", Toast.LENGTH_SHORT)
//                        .show()
//                    activity!!.finish()
//                }
//            }
//
//            override fun onFailure(call: retrofit2.Call<Question?>, t: Throwable) {
//                Log.e("ERROR: ", t.message!!)
//            }
//        })

        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getInstance(requireContext()).latexDataDao()
            dao.update(question!!)
        }
    }

    fun createQuestion(question: Question?) {
        val call = mQuestionService!!.createQuestion(question)
        call?.enqueue(object : Callback<Question?> {
            override fun onResponse(
                call: retrofit2.Call<Question?>,
                response: Response<Question?>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Question created successfully!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Question?>, t: Throwable) {
                Log.e("ERROR: ", t.message!!)
            }
        })
    }

    fun deleteQuestion(id: String?) {
        val call = mQuestionService!!.deleteQuestion(id)
        call!!.enqueue(object : Callback<Question?> {
            override fun onResponse(
                call: retrofit2.Call<Question?>,
                response: Response<Question?>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Question deleted successfully!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Question?>, t: Throwable) {
                Log.e("ERROR: ", t.message!!)
            }
        })
    }

    companion object {
        val TRIM_UNICODE_PATTERN =
            Pattern.compile("^\\p{Blank}*(.*)\\p{Blank}$", Pattern.UNICODE_CASE)
        val SPLIT_SPACE_UNICODE_PATTERN = Pattern.compile("\\p{Blank}", Pattern.UNICODE_CASE)
        fun newInstance(): KatexFragment {
            return KatexFragment()
        }

        fun trimSplitUnicodeBySpace(act: Activity?, str: String): Array<String>? {
            Toast.makeText(act, "trimSplitUnicodeBySpace: $str", Toast.LENGTH_SHORT).show()
            return null
        }
    }
}