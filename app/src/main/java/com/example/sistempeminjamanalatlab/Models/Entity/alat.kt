package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class Alat(
    @SerializedName("id") val id: Long,
    @SerializedName("kode_alat") val kodeAlat: String,
    @SerializedName("nama_alat") val namaAlat: String,
    @SerializedName("merk") val merk: String?,
    @SerializedName("spesifikasi") val spesifikasi: String?,
    @SerializedName("lokasi_penyimpanan") val lokasiPenyimpanan: String?,
    @SerializedName("stok_total") val stokTotal: Int,
    @SerializedName("stok_tersedia") val stokTersedia: Int,
    @SerializedName("kondisi_fisik") val kondisiFisik: String,
    @SerializedName("status_ketersediaan") val statusKetersediaan: String,
    @SerializedName("foto") val foto: String?,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("kategori") val kategori: KategoriAlat?
)