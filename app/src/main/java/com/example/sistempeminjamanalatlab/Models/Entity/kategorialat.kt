package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class KategoriAlat(
    @SerializedName("id") val id: Long,
    @SerializedName("nama_kategori") val namaKategori: String,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("created_at") val createdAt: String
)