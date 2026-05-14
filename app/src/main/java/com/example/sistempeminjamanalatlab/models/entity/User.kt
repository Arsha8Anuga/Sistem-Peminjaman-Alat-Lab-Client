package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Long,
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,

    // Role: mahasiswa, laboran, asisten, admin
    @SerializedName("role") val role: String,

    @SerializedName("nim_nip") val nimNip: String?,
    @SerializedName("no_hp") val noHp: String?,

    // Tambahkan ini: Untuk menyimpan URL foto profil user
    @SerializedName("foto") val foto: String?,

    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String? = null
)