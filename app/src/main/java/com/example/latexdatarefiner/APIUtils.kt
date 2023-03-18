package com.example.latexdatarefiner

object APIUtils {
    const val API_URL = "http://192.168.43.143:5000/api/v1/"

    val questionService: QuestionService
        get() = RetrofitClient.getClient(API_URL).create(QuestionService::class.java)
}
