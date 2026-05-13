package com.example.sistempeminjamanalatlab.API

import com.example.sistempeminjamanalatlab.models.entity.*
import com.example.sistempeminjamanalatlab.models.request.*
import com.example.sistempeminjamanalatlab.models.response.*
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    // ─────────────────────────────────────────────────────
    // AUTH
    // ─────────────────────────────────────────────────────

    @POST("api/auth/login")
    fun loginUser(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("api/auth/register")
    fun registerUser(
        @Body request: RegisterRequest
    ): Call<WrappedResponse<User>>

    @GET("api/auth/me")
    fun getMe(
        @Header("Authorization") token: String
    ): Call<WrappedResponse<User>>

    // ─────────────────────────────────────────────────────
    // USER (ADMIN)
    // ─────────────────────────────────────────────────────

    @GET("api/users")
    fun getAllUsers(
        @Header("Authorization") token: String
    ): Call<WrappedResponse<List<User>>>

    @GET("api/users/{id}")
    fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<WrappedResponse<User>>

    @PUT("api/users/{id}")
    fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: UserEditRequest
    ): Call<WrappedResponse<User>>

    @DELETE("api/users/{id}")
    fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<BaseResponse>

    // ─────────────────────────────────────────────────────
    // KATEGORI ALAT
    // ─────────────────────────────────────────────────────

    @GET("api/kategori")
    fun getAllKategori(
        @Header("Authorization") token: String
    ): Call<WrappedResponse<List<KategoriAlat>>>

    @POST("api/kategori")
    fun createKategori(
        @Header("Authorization") token: String,
        @Body request: KategoriRequest
    ): Call<WrappedResponse<KategoriAlat>>

    @PUT("api/kategori/{id}")
    fun updateKategori(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: KategoriRequest
    ): Call<WrappedResponse<KategoriAlat>>

    @DELETE("api/kategori/{id}")
    fun deleteKategori(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<BaseResponse>

    // ─────────────────────────────────────────────────────
    // ALAT
    // ─────────────────────────────────────────────────────

    @GET("api/alat")
    fun getAllAlat(
        @Header("Authorization") token: String,
        @Query("kategori_id") kategoriId: Long? = null,
        @Query("keyword") keyword: String? = null
    ): Call<WrappedResponse<List<Alat>>>

    @GET("api/alat/{id}")
    fun getAlatById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<WrappedResponse<Alat>>

    @POST("api/alat")
    fun createAlat(
        @Header("Authorization") token: String,
        @Body request: AlatCreateRequest
    ): Call<WrappedResponse<Alat>>

    @PUT("api/alat/{id}")
    fun updateAlat(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: AlatUpdateRequest
    ): Call<WrappedResponse<Alat>>

    @DELETE("api/alat/{id}")
    fun deleteAlat(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<BaseResponse>

    // ─────────────────────────────────────────────────────
    // PEMINJAMAN
    // ─────────────────────────────────────────────────────

    @GET("api/peminjaman")
    fun getMyPeminjaman(
        @Header("Authorization") token: String
    ): Call<WrappedResponse<List<Peminjaman>>>

    @GET("api/peminjaman/semua")
    fun getAllPeminjaman(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null
    ): Call<WrappedResponse<List<Peminjaman>>>

    @GET("api/peminjaman/aktif")
    fun getPeminjamanAktif(
        @Header("Authorization") token: String
    ): Call<WrappedResponse<List<Peminjaman>>>

    @GET("api/peminjaman/pending")
    fun getPeminjamanPending(
        @Header("Authorization") token: String
    ): Call<WrappedResponse<List<Peminjaman>>>

    @GET("api/peminjaman/{id}")
    fun getPeminjamanById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<WrappedResponse<Peminjaman>>

    @POST("api/peminjaman")
    fun createPeminjaman(
        @Header("Authorization") token: String,
        @Body request: PinjamRequest
    ): Call<WrappedResponse<Peminjaman>>

    @PUT("api/peminjaman/{id}/approval")
    fun approvalPeminjaman(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: ApprovalRequest
    ): Call<WrappedResponse<Peminjaman>>

    @PUT("api/peminjaman/{id}/batal")
    fun batalPeminjaman(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Call<WrappedResponse<Peminjaman>>

    // ─────────────────────────────────────────────────────
    // PENGEMBALIAN
    // ─────────────────────────────────────────────────────

    @POST("api/pengembalian")
    fun submitPengembalian(
        @Header("Authorization") token: String,
        @Body request: KembaliRequest
    ): Call<WrappedResponse<Pengembalian>>

    @GET("api/pengembalian/{peminjaman_id}")
    fun getPengembalianByPeminjaman(
        @Header("Authorization") token: String,
        @Path("peminjaman_id") peminjamanId: Long
    ): Call<WrappedResponse<Pengembalian>>

    // ─────────────────────────────────────────────────────
    // KONDISI LOG
    // ─────────────────────────────────────────────────────

    @GET("api/alat/{id}/kondisi-log")
    fun getKondisiLog(
        @Header("Authorization") token: String,
        @Path("id") alatId: Long
    ): Call<WrappedResponse<List<KondisiLog>>>
}