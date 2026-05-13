package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

data class DetailPeminjamanRequest(
    @SerializedName("alat_id") val alatId: Long,
    @SerializedName("jumlah") val jumlah: Int
)

data class PinjamRequest(
    @SerializedName("tanggal_pinjam") val tanggalPinjam: String,             // "yyyy-MM-dd"
    @SerializedName("tanggal_rencana_kembali") val tanggalRencanaKembali: String,
    @SerializedName("catatan") val catatan: String? = null,
    @SerializedName("detail") val detail: List<DetailPeminjamanRequest>
)

data class ApprovalRequest(
    @SerializedName("status") val status: String,   // "disetujui" / "ditolak"
    @SerializedName("catatan") val catatan: String? = null
)