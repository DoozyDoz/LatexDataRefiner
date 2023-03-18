package com.example.latexdatarefiner

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitClient {
    private val okHttpClient = OkHttpClient.Builder().build()

    fun getClient(url:String?):Retrofit= Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

}

