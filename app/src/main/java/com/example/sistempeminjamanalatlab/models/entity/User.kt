package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Long,
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,

    // Sesuai ENUM: mahasiswa, laboran, asisten, admin
    @SerializedName("role") val role: String,

    @SerializedName("nim_nip") val nimNip: String?,
    @SerializedName("no_hp") val noHp: String?,

    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String? = null
)