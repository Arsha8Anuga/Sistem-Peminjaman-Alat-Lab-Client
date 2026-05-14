package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class Alat(
    @SerializedName("id") val id: Long,
    @SerializedName("kategori_id") val kategoriId: Long,
    @SerializedName("kode_alat") val kodeAlat: String,
    @SerializedName("nama_alat") val namaAlat: String,
    @SerializedName("merk") val merk: String?,
    @SerializedName("spesifikasi") val spesifikasi: String?,
    @SerializedName("lokasi_penyimpanan") val lokasiPenyimpanan: String?,
    @SerializedName("stok_total") val stokTotal: Int,
    @SerializedName("stok_tersedia") val stokTersedia: Int,

    // Status (Gunakan String jika dari API berupa Enum)
    @SerializedName("kondisi_fisik") val kondisiFisik: String, // misal: "Baik", "Rusak"
    @SerializedName("status_ketersediaan") val statusKetersediaan: String, // misal: "Tersedia", "Kosong"

    @SerializedName("foto") val foto: String?,
    @SerializedName("deskripsi") val deskripsi: String?,

    // Metadata
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,

    // Relasi hasil JOIN
    @SerializedName("kategori") val kategori: KategoriAlat? = null
)