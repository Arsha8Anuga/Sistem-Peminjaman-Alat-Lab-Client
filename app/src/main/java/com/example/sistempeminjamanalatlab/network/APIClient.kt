package com.example.sistempeminjamanalatlab.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {
    private const val BASE_URL = "https://c6c8-114-10-45-245.ngrok-free.app/"

    // 2. Logging Interceptor agar kamu bisa melihat request & response di Logcat
    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 3. OkHttpClient untuk mengatur timeout dan logging
    private val client = OkHttpClient.Builder()
        .addInterceptor(logger) // Menampilkan log network
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // 4. Inisialisasi Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 5. Fungsi untuk memanggil Interface API (ApiService)
    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}