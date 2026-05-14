package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

// --- KATEGORI ---
data class KategoriRequest(
    @SerializedName("nama_kategori") val namaKategori: String,
    @SerializedName("deskripsi") val deskripsi: String? = null
)

// --- ALAT ---
data class AlatCreateRequest(
    @SerializedName("kategori_id") val kategoriId: Long,
    @SerializedName("kode_alat") val kodeAlat: String,
    @SerializedName("nama_alat") val namaAlat: String,
    @SerializedName("merk") val merk: String? = null,
    @SerializedName("spesifikasi") val spesifikasi: String? = null,
    @SerializedName("lokasi_penyimpanan") val lokasiPenyimpanan: String? = null,
    @SerializedName("stok_total") val stokTotal: Int,
    @SerializedName("kondisi_fisik") val kondisiFisik: String = "baik",
    @SerializedName("status_ketersediaan") val statusKetersediaan: String = "tersedia",
    @SerializedName("deskripsi") val deskripsi: String? = null
    // Field foto dihapus karena di APIService menggunakan @Multipart terpisah
)

data class AlatUpdateRequest(
    @SerializedName("kategori_id") val kategoriId: Long? = null,
    @SerializedName("kode_alat") val kodeAlat: String? = null,
    @SerializedName("nama_alat") val namaAlat: String? = null,
    @SerializedName("merk") val merk: String? = null,
    @SerializedName("spesifikasi") val spesifikasi: String? = null,
    @SerializedName("lokasi_penyimpanan") val lokasiPenyimpanan: String? = null,
    @SerializedName("stok_total") val stokTotal: Int? = null,
    @SerializedName("stok_tersedia") val stokTersedia: Int? = null,
    @SerializedName("kondisi_fisik") val kondisiFisik: String? = null,
    @SerializedName("status_ketersediaan") val statusKetersediaan: String? = null,
    @SerializedName("deskripsi") val deskripsi: String? = null
)

// --- LOG KONDISI ---
data class KondisiLogRequest(
    @SerializedName("alat_id") val alatId: Long,
    @SerializedName("peminjaman_id") val peminjamanId: Long? = null,
    @SerializedName("kondisi") val kondisi: String, // baik, rusak_ringan, rusak_berat, maintenance, hilang
    @SerializedName("catatan") val catatan: String?
)