package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

data class UserEditRequest(
    @SerializedName("nama") val nama: String? = null,
    @SerializedName("no_hp") val noHp: String? = null,
    @SerializedName("nim_nip") val nimNip: String? = null
)