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
    @SerializedName("kondisi_fisik") val kondisiFisik: String = "baik",
    @SerializedName("foto") val foto: String? = null,
    @SerializedName("deskripsi") val deskripsi: String? = null
)

data class AlatUpdateRequest(
    @SerializedName("nama_alat") val namaAlat: String? = null,
    @SerializedName("merk") val merk: String? = null,
    @SerializedName("spesifikasi") val spesifikasi: String? = null,
    @SerializedName("lokasi_penyimpanan") val lokasiPenyimpanan: String? = null,
    @SerializedName("stok_total") val stokTotal: Int? = null,
    @SerializedName("kondisi_fisik") val kondisiFisik: String? = null,
    @SerializedName("status_ketersediaan") val statusKetersediaan: String? = null,
    @SerializedName("foto") val foto: String? = null,
    @SerializedName("deskripsi") val deskripsi: String? = null
)