package com.example.sistempeminjamanalatlab.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.sistempeminjamanalatlab.models.entity.Peminjaman
import com.example.sistempeminjamanalatlab.models.response.WrappedResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("peminjaman")
    fun getAllPeminjaman(@Header("Authorization") token: String): Call<WrappedResponse<List<Peminjaman>>>

    @PUT("peminjaman/{id}/approve")
    fun approvePeminjaman(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<WrappedResponse<Peminjaman>>

    @PUT("peminjaman/{id}/reject")
    fun rejectPeminjaman(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Query("catatan") catatan: String
    ): Call<WrappedResponse<Peminjaman>>
}

object APIClient {
    // 1. Sesuaikan URL dengan server Python kamu
    // Gunakan 10.0.2.2 jika running di emulator Android Studio
    // Gunakan IP WiFi laptop jika running di HP fisik (misal: 192.168.1.5)
    private const val BASE_URL = "http://10.0.2.2:5000/api/v1/"

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