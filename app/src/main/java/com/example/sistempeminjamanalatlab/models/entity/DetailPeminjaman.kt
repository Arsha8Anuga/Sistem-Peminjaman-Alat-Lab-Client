package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class DetailPeminjaman(
    @SerializedName("id") val id: Long,

    // Foreign Key ke tabel Peminjaman
    @SerializedName("peminjaman_id") val peminjamanId: Long,

    @SerializedName("alat_id") val alatId: Long,
    @SerializedName("jumlah") val jumlah: Int,

    // Status kondisi (Gunakan null-safe karena diisi saat kembali)
    @SerializedName("kondisi_awal") val kondisiAwal: String?,
    @SerializedName("kondisi_akhir") val kondisiAkhir: String?,

    @SerializedName("catatan_pengembalian") val catatanPengembalian: String?,

    // Relasi objek (Hasil JOIN dari backend)
    @SerializedName("alat") val alat: Alat? = null
)
