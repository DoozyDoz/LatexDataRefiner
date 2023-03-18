package com.example.latexdatarefiner

import retrofit2.Call
import retrofit2.http.*


interface QuestionService {
    @get:GET("questions")
    val questions: Call<Any?>?

    @POST("questions")
    fun createQuestion(@Body question: Question?): Call<Question?>?

    @PATCH("questions/{id}")
    fun updateQuestion(@Path("id") id: String?, @Body question: Question?): Call<Question?>?

    @DELETE("questions/{id}")
    fun deleteQuestion(@Path("id") id: String?): Call<Question?>?
}
