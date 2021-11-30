package com.example.myapplication.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


const val POST_PER_PAGE = 1

//game
const val BASE_URL = "https://api.rawg.io/api/"

object TheGameDBClient {
    fun getClient(): TheGameDBInterface {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TheGameDBInterface::class.java)
    }
}

