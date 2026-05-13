package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

data class DetailKondisiKembali(
    @SerializedName("detail_id") val detailId: Long,
    @SerializedName("kondisi_akhir") val kondisiAkhir: String,  // baik / rusak_ringan / rusak_berat / hilang
    @SerializedName("catatan_pengembalian") val catatanPengembalian: String? = null
)

data class KembaliRequest(
    @SerializedName("peminjaman_id") val peminjamanId: Long,
    @SerializedName("denda") val denda: Double = 0.0,
    @SerializedName("catatan") val catatan: String? = null,
    @SerializedName("detail_kondisi") val detailKondisi: List<DetailKondisiKembali>
)