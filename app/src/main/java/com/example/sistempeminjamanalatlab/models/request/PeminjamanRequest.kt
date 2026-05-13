package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

data class PinjamRequest(
    @SerializedName("mahasiswa_id") val mahasiswaId: Long, // Tambahkan ini
    @SerializedName("tanggal_pinjam") val tanggalPinjam: String,
    @SerializedName("tanggal_rencana_kembali") val tanggalRencanaKembali: String,
    @SerializedName("catatan") val catatan: String? = null,
    @SerializedName("detail") val detail: List<DetailPeminjamanRequest>
)

data class DetailPeminjamanRequest(
    @SerializedName("alat_id") val alatId: Long,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("kondisi_awal") val kondisiAwal: String = "baik" // Tambahkan ini
)

data class ApprovalRequest(
    @SerializedName("status") val status: String,   // "disetujui" / "ditolak"
    @SerializedName("disetujui_oleh") val disetujuiOleh: Long, // Tambahkan ini (ID Staff/Laboran)
    @SerializedName("catatan") val catatan: String? = null
)