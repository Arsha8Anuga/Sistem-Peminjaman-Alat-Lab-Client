package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class DetailPeminjaman(
    @SerializedName("id") val id: Long,

    // Tambahkan ini: Relasi ke tabel utama
    @SerializedName("peminjaman_id") val peminjamanId: Long,

    @SerializedName("alat_id") val alatId: Long,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("kondisi_awal") val kondisiAwal: String?,
    @SerializedName("kondisi_akhir") val kondisiAkhir: String?,
    @SerializedName("catatan_pengembalian") val catatanPengembalian: String?,
    @SerializedName("alat") val alat: Alat?
)
