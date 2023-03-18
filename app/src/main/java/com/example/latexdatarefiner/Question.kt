package com.example.latexdatarefiner

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Question(
    @field:Json(name = "qtn_text") val text: String?,
    @field:Json(name = "year") var year: Int?,
    @field:Json(name = "paper") var paper: Int?,
    @field:Json(name = "section") var section: String?,
    @field:Json(name = "topic") var topic: String?,
    @field:Json(name = "answer") var answer: String?,
    @field:Json(name = "katex_question") var katex_question: String?,
    @field:Json(name = "katex_answer") var katex_answer: String?,
    @field:Json(name = "edited") var edited: Boolean?,
) {
    @field:Json(name = "_id")
    var id: String? = null

}
