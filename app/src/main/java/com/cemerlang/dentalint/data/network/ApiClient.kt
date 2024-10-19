package com.cemerlang.dentalint.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private val okHttpClient = buildOkHttpClient()
//    private const val BASE_URL = "http://10.0.2.2:3000" // Emulator to localhost
//    private const val BASE_URL = "http://127.0.0.1:3000" // Connected device to localhost
    private const val BASE_URL = "https://hello-world-413806.et.r.appspot.com" // Server

    fun getApiInstance(): ApiService {
        val builder = Retrofit.Builder().baseUrl("$BASE_URL/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return builder.create(ApiService::class.java)
    }

    private fun buildOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }
}