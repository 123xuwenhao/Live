package com.example.live.logic.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServerCreator {



    private val loggingInterceptor = HttpLoggingInterceptor { message -> //打印retrofit日志
        Log.i("RetrofitLog", "retrofitBack = $message")
    }.apply { level = HttpLoggingInterceptor.Level.BODY }
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    //private const val BASE_URL =  "http://10.0.2.2:8082/streaming/"
    const val BASE_URL =  "http://47.99.171.180:8082/streaming/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    /*
    private val client: OkHttpClient = OkHttpClient.Builder().build()
    private const val BASE_URL =  "http://47.99.171.180:8082/streaming/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

     */

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}