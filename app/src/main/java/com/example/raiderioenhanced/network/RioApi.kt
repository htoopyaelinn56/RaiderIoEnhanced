package com.example.raiderioenhanced.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

private val URL = "https://raider.io"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(URL)
    .build()

interface RioApiService{
    @GET
    suspend fun getData(@Url url : String) : String
}

object RioApi{
    val retrofitService : RioApiService by lazy {
        retrofit.create(RioApiService::class.java)
    }
}