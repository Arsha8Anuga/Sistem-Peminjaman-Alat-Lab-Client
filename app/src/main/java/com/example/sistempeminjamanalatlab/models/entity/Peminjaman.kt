package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class Peminjaman(
    @SerializedName("id") val id: Long,
    @SerializedName("kode_peminjaman") val kodePeminjaman: String,
    @SerializedName("mahasiswa_id") val mahasiswaId: Long,

    // Staff yang memproses (Approve/Reject)
    @SerializedName("disetujui_oleh") val disetujuiOleh: Long?,

    @SerializedName("tanggal_pengajuan") val tanggalPengajuan: String,
    @SerializedName("tanggal_pinjam") val tanggalPinjam: String?,
    @SerializedName("tanggal_rencana_kembali") val tanggalRencanaKembali: String?,

    // Status: pending, approved, rejected, dipinjam, kembali, cancelled
    @SerializedName("status") val status: String,
    @SerializedName("catatan") val catatan: String?,

    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,

    // List alat yang dipinjam
    @SerializedName("details") val details: List<DetailPeminjaman> = emptyList(),

    // Data Mahasiswa (peminjam)
    @SerializedName("mahasiswa") val mahasiswa: User?,

    // Data Laboran (penyetuju)
    @SerializedName("staff_penyetuju") val staffPenyetuju: User? = null,

    // Tambahkan ini jika Backend menyertakan data pengembalian
    @SerializedName("pengembalian") val pengembalian: Pengembalian? = null
)