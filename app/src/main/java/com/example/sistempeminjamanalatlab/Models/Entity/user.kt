package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Long,
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("nim_nip") val nimNip: String?,
    @SerializedName("no_hp") val noHp: String?,
    @SerializedName("created_at") val createdAt: String
)