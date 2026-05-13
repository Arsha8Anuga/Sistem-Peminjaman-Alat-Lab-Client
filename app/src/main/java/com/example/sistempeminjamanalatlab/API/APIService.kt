package com.example.sistempeminjamanalatlab.API

import com.example.sistempeminjamanalatlab.Models.
import com.example.sistempeminjamanalatlab.Models.
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    // --- AUTHENTICATION ---
    @POST("placeholder/login") // Path akan diubah sesuai doc API Python
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    // --- MANAJEMEN USER (ADMIN) ---
    @GET("placeholder/users")
    fun getAllUsers(): Call<List<UserResponse>>

    @PUT("placeholder/users/{id}")
    fun updateUser(
        @Path("id") id: Long,
        @Body request: UserEditRequest
    ): Call<BaseResponse>

    // --- INVENTARIS ALAT ---
    @GET("placeholder/alat")
    fun getAllAlat(): Call<List<AlatResponse>>

    @GET("placeholder/alat/kategori/{kategori_id}")
    fun getAlatByKategori(@Path("kategori_id") id: Long): Call<List<AlatResponse>>

    // --- PEMINJAMAN & PENGEMBALIAN ---
    @GET("placeholder/peminjaman/aktif")
    fun getPeminjamanAktif(): Call<List<PeminjamanResponse>>

    @POST("placeholder/peminjaman")
    fun createPeminjaman(@Body request: PinjamRequest): Call<BaseResponse>

    @POST("placeholder/pengembalian")
    fun submitPengembalian(@Body request: KembaliRequest): Call<BaseResponse>
}