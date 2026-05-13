package com.example.sistempeminjamanalatlab.api


import com.example.sistempeminjamanalatlab.models.request.*
import com.example.sistempeminjamanalatlab.models.response.*
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    // ─── AUTH ─────────────────────────────────────────────────
    @POST("api/auth/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/auth/me")
    fun getMe(@Header("Authorization") token: String): Call<UserResponse>

    // ─── USER (ADMIN) ──────────────────────────────────────────
    @GET("api/users")
    fun getAllUsers(@Header("Authorization") token: String): Call<UserListResponse>

    @GET("api/users/{id}")
    fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<UserResponse>

    @PUT("api/users/{id}")
    fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: UserUpdateRequest // Sesuai file UserRequest.kt
    ): Call<UserResponse>

    @DELETE("api/users/{id}")
    fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<BaseResponse>

    // ─── KATEGORI ALAT ─────────────────────────────────────────
    @GET("api/kategori")
    fun getAllKategori(
        @Header("Authorization") token: String
    ): Call<KategoriListResponse> // Jauh lebih ringkas

    @POST("api/kategori")
    fun createKategori(
        @Header("Authorization") token: String,
        @Body request: KategoriRequest
    ): Call<KategoriResponse>

    @PUT("api/kategori/{id}")
    fun updateKategori(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: KategoriRequest
    ): Call<KategoriResponse>

    // ─── ALAT ──────────────────────────────────────────────────
    @GET("api/alat")
    fun getAllAlat(
        @Header("Authorization") token: String,
        @Query("kategori_id") kategoriId: Long? = null,
        @Query("keyword") keyword: String? = null
    ): Call<AlatListResponse>

    @POST("api/alat")
    fun createAlat(
        @Header("Authorization") token: String,
        @Body request: AlatCreateRequest
    ): Call<AlatResponse>

    @PUT("api/alat/{id}")
    fun updateAlat(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: AlatUpdateRequest
    ): Call<AlatResponse>

    @GET("api/alat/{id}")
    fun getAlatById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<AlatResponse> // Mengembalikan 1 objek Alat (Wrapped)

    @DELETE("api/alat/{id}")
    fun deleteAlat(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<BaseResponse>

    // ─── PEMINJAMAN ────────────────────────────────────────────
    @GET("api/peminjaman")
    fun getMyPeminjaman(@Header("Authorization") token: String): Call<PeminjamanListResponse>

    @GET("api/peminjaman/semua")
    fun getAllPeminjaman(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null
    ): Call<PeminjamanListResponse>

    @POST("api/peminjaman")
    fun createPeminjaman(
        @Header("Authorization") token: String,
        @Body request: PinjamRequest
    ): Call<PeminjamanResponse>

    @PUT("api/peminjaman/{id}/approval")
    fun approvalPeminjaman(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: ApprovalRequest
    ): Call<PeminjamanResponse>

    // ─── PENGEMBALIAN ──────────────────────────────────────────
    @POST("api/pengembalian")
    fun submitPengembalian(
        @Header("Authorization") token: String,
        @Body request: PengembalianRequest // Sesuai file PeminjamanRequest.kt
    ): Call<PengembalianResponse>

    @GET("api/pengembalian/{peminjaman_id}")
    fun getPengembalianDetail(
        @Header("Authorization") token: String,
        @Path("peminjaman_id") peminjamanId: Long
    ): Call<PengembalianResponse>

    // ─── KONDISI LOG ───────────────────────────────────────────
    @GET("api/alat/{id}/kondisi-log")
    fun getKondisiLog(
        @Header("Authorization") token: String,
        @Path("id") alatId: Long
    ): Call<KondisiLogListResponse>
}