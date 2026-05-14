package com.example.sistempeminjamanalatlab.models.request

import com.google.gson.annotations.SerializedName

data class PeminjamanRequest(
    // mahasiswa_id dihapus (diambil dari Token oleh Backend)
    @SerializedName("tanggal_rencana_kembali") val tanggalRencanaKembali: String,
    @SerializedName("catatan") val catatan: String? = null,
    @SerializedName("details") val details: List<DetailPeminjamanRequest>
    // Gunakan "details" agar sinkron dengan penamaan jamak biasanya
)

data class DetailPeminjamanRequest(
    @SerializedName("alat_id") val alatId: Long,
    @SerializedName("jumlah") val jumlah: Int
    // kondisi_awal dihapus (ditangani oleh logic server/laboran saat verifikasi)
)

data class ApprovalRequest(
    // status dihapus jika menggunakan endpoint /approve atau /reject secara terpisah
    @SerializedName("catatan") val catatan: String? = null
    // disetujui_oleh dihapus (diambil dari Token Laboran)
)