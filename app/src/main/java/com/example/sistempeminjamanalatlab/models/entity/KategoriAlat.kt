package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class KategoriAlat(
    @SerializedName("id") val id: Long,
    @SerializedName("nama_kategori") val namaKategori: String,
    @SerializedName("deskripsi") val deskripsi: String?,

    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String? = null
) {
    // Tips: Tambahkan fungsi ini jika kamu ingin menampilkan
    // nama kategori langsung di Spinner (Dropdown) saat tambah alat.
    override fun toString(): String {
        return namaKategori
    }
}