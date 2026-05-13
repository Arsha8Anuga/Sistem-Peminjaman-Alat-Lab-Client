package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

data class UserCreateRequest(
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("role") val role: String, // mahasiswa, laboran, asisten, admin
    @SerializedName("nim_nip") val nimNip: String?,
    @SerializedName("no_hp") val noHp: String?
)

// Digunakan Admin untuk mengedit data User yang sudah ada
data class UserUpdateRequest(
    @SerializedName("nama") val nama: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("nim_nip") val nimNip: String? = null,
    @SerializedName("no_hp") val noHp: String? = null,
    @SerializedName("password") val password: String? = null
)