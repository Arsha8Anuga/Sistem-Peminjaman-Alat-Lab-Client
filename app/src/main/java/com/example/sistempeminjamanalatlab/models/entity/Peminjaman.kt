package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class Peminjaman(
    @SerializedName("id") val id: Long,
    @SerializedName("kode_peminjaman") val kodePeminjaman: String,
    @SerializedName("mahasiswa_id") val mahasiswaId: Long,

    // Tambahkan ini: Untuk mencatat staff/laboran yang menyetujui
    @SerializedName("disetujui_oleh") val disetujuiOleh: Long?,

    @SerializedName("tanggal_pengajuan") val tanggalPengajuan: String,
    @SerializedName("tanggal_pinjam") val tanggalPinjam: String?,
    @SerializedName("tanggal_rencana_kembali") val tanggalRencanaKembali: String?,
    @SerializedName("status") val status: String,
    @SerializedName("catatan") val catatan: String?,

    // Tambahkan timestamps untuk sinkronisasi database
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,

    @SerializedName("detail") val detail: List<DetailPeminjaman> = emptyList(),
    @SerializedName("mahasiswa") val mahasiswa: User?,

    // Tambahkan ini jika API mengirimkan data staff yang menyetujui (Join hasil users)
    @SerializedName("staff_penyetuju") val staffPenyetuju: User? = null
)