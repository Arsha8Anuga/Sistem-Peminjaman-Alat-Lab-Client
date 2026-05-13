package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

data class KategoriRequest(
    @SerializedName("nama_kategori") val namaKategori: String,
    @SerializedName("deskripsi") val deskripsi: String? = null
)

data class AlatCreateRequest(
    @SerializedName("kategori_id") val kategoriId: Long,
    @SerializedName("kode_alat") val kodeAlat: String,
    @SerializedName("nama_alat") val namaAlat: String,
    @SerializedName("merk") val merk: String? = null,
    @SerializedName("spesifikasi") val spesifikasi: String? = null,
    @SerializedName("lokasi_penyimpanan") val lokasiPenyimpanan: String? = null,
    @SerializedName("stok_total") val stokTotal: Int,
    @SerializedName("stok_tersedia") val stokTersedia: Int, // Tambahkan ini
    @SerializedName("kondisi_fisik") val kondisiFisik: String = "baik",
    @SerializedName("status_ketersediaan") val statusKetersediaan: String = "tersedia", // Tambahkan ini
    @SerializedName("foto") val foto: String? = null,
    @SerializedName("deskripsi") val deskripsi: String? = null
)

data class AlatUpdateRequest(
    @SerializedName("kategori_id") val kategoriId: Long? = null, // Tambahkan ini
    @SerializedName("kode_alat") val kodeAlat: String? = null,   // Tambahkan ini
    @SerializedName("nama_alat") val namaAlat: String? = null,
    @SerializedName("merk") val merk: String? = null,
    @SerializedName("spesifikasi") val spesifikasi: String? = null,
    @SerializedName("lokasi_penyimpanan") val lokasiPenyimpanan: String? = null,
    @SerializedName("stok_total") val stokTotal: Int? = null,
    @SerializedName("stok_tersedia") val stokTersedia: Int? = null, // Tambahkan ini
    @SerializedName("kondisi_fisik") val kondisiFisik: String? = null,
    @SerializedName("status_ketersediaan") val statusKetersediaan: String? = null,
    @SerializedName("foto") val foto: String? = null,
    @SerializedName("deskripsi") val deskripsi: String? = null
)

data class KondisiLogRequest(
    @SerializedName("alat_id") val alatId: Long,

    // Nullable karena tidak selalu log kondisi berasal dari peminjaman
    @SerializedName("peminjaman_id") val peminjamanId: Long? = null,

    // ENUM: baik, rusak_ringan, rusak_berat, maintenance, hilang
    @SerializedName("kondisi") val kondisi: String,

    @SerializedName("catatan") val catatan: String?,

    // ID User (Staff/Laboran) yang melakukan pencatatan
    @SerializedName("dicatat_oleh") val dicatatOleh: Long
)