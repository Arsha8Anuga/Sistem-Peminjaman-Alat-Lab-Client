package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("role") val role: String = "mahasiswa",
    @SerializedName("nim_nip") val nimNip: String? = null,
    @SerializedName("no_hp") val noHp: String? = null
)