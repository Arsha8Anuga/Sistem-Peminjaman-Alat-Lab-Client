package com.example.sistempeminjamanalatlab.api

import com.example.sistempeminjamanalatlab.models.entity.Alat
import com.example.sistempeminjamanalatlab.models.entity.KategoriAlat
import com.example.sistempeminjamanalatlab.models.request.*
import com.example.sistempeminjamanalatlab.models.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    // ─── AUTH ─────────────────────────────────────────────────
    @POST("auth/login")
    fun loginUser(@Body request: LoginRequest): Call<ActualLoginResponse>

    // ─── ALAT ──────────────────────────────────────────────────
    @GET("alat/")
    fun getAllAlat(@Header("Authorization") token: String): Call<List<Alat>>

    @POST("alat/")
    fun createAlat(
        @Header("Authorization") token: String,
        @Query("nama_alat") namaAlat: String,
        @Query("kode_alat") kodeAlat: String,
        @Query("kategori_id") kategoriId: Long,
        @Query("stok_total") stokTotal: Int,
        @Query("stok_tersedia") stokTersedia: Int, // Wajib ada karena divalidasi Python
        @Query("kondisi_fisik") kondisiFisik: String,
        @Query("status_ketersediaan") statusKetersediaan: String, // Wajib ada karena divalidasi Python
        @Query("lokasi_penyimpanan") lokasiPenyimpanan: String?  // Wajib ada karena divalidasi Python
    ): Call<AlatResponse>

    @GET("alat/{alat_id}")
    fun getAlatById(@Header("Authorization") token: String, @Path("alat_id") id: Long): Call<AlatResponse>

    @PUT("alat/{id}")
    fun updateAlat(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Query("nama_alat") namaAlat: String,
        @Query("kode_alat") kodeAlat: String,
        @Query("kategori_id") kategoriId: Long,
        @Query("stok_total") stokTotal: Int,
        @Query("stok_tersedia") stokTersedia: Int,
        @Query("kondisi_fisik") kondisiFisik: String,
        @Query("status_ketersediaan") statusKetersediaan: String,
        @Query("lokasi_penyimpanan") lokasiPenyimpanan: String?
    ): Call<AlatResponse>

    @DELETE("alat/{alat_id}")
    fun deleteAlat(@Header("Authorization") token: String, @Path("alat_id") id: Long): Call<BaseResponse>

    // ─── KATEGORI ──────────────────────────────────────────────
    @GET("kategori/")
    fun getAllKategori(@Header("Authorization") token: String): Call<List<KategoriAlat>>

    @POST("kategori/")
    fun createKategori(@Header("Authorization") token: String, @Body request: KategoriRequest): Call<KategoriResponse>

    @GET("kategori/{kategori_id}")
    fun getKategoriById(@Header("Authorization") token: String, @Path("kategori_id") id: Long): Call<KategoriResponse>

    @PUT("kategori/{kategori_id}")
    fun updateKategori(@Header("Authorization") token: String, @Path("kategori_id") id: Long, @Body request: KategoriRequest): Call<KategoriResponse>

    @DELETE("kategori/{kategori_id}")
    fun deleteKategori(@Header("Authorization") token: String, @Path("kategori_id") id: Long): Call<BaseResponse>

    // ─── PEMINJAMAN ────────────────────────────────────────────
    @GET("peminjaman/")
    fun getListPeminjaman(@Header("Authorization") token: String): Call<PeminjamanListResponse>

    @POST("peminjaman/")
    fun createPeminjaman(@Header("Authorization") token: String, @Body request: PeminjamanRequest): Call<PeminjamanResponse>

    @GET("peminjaman/{peminjaman_id}")
    fun getDetailPeminjaman(@Header("Authorization") token: String, @Path("peminjaman_id") id: Long): Call<PeminjamanResponse>

    @PUT("peminjaman/{peminjaman_id}/approve")
    fun approvePeminjaman(@Header("Authorization") token: String, @Path("peminjaman_id") id: Long): Call<BaseResponse>

    @PUT("peminjaman/{peminjaman_id}/reject")
    fun rejectPeminjaman(@Header("Authorization") token: String, @Path("peminjaman_id") id: Long): Call<BaseResponse>

    @PUT("peminjaman/{peminjaman_id}/ambil")
    fun ambilAlat(@Header("Authorization") token: String, @Path("peminjaman_id") id: Long): Call<BaseResponse>

    @PUT("peminjaman/{peminjaman_id}/cancel")
    fun cancelPeminjaman(@Header("Authorization") token: String, @Path("peminjaman_id") id: Long): Call<BaseResponse>

    // ─── PENGEMBALIAN ──────────────────────────────────────────
    @GET("pengembalian/{peminjaman_id}")
    fun getDetailPengembalian(@Header("Authorization") token: String, @Path("peminjaman_id") id: Long): Call<PengembalianResponse>

    @POST("pengembalian/")
    fun createPengembalian(@Header("Authorization") token: String, @Body request: PengembalianRequest): Call<PengembalianResponse>

    @PUT("pengembalian/{peminjaman_id}/verify")
    fun verifyPengembalian(
        @Header("Authorization") token: String,
        @Path("peminjaman_id") id: Long,
        @Body request: VerifyPengembalianRequest // TAMBAHKAN INI
    ): Call<BaseResponse>

    // ─── UPLOAD ────────────────────────────────────────────────
    @Multipart
    @POST("upload/user/{user_id}")
    fun uploadUserPhoto(@Header("Authorization") token: String, @Path("user_id") id: Long, @Part foto: MultipartBody.Part): Call<BaseResponse>

    @Multipart
    @POST("upload/alat/{alat_id}")
    fun uploadAlatPhoto(@Header("Authorization") token: String, @Path("alat_id") id: Long, @Part foto: MultipartBody.Part): Call<BaseResponse>
}