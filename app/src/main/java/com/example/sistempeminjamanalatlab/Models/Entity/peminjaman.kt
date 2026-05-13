package com.example.sistempeminjamanalatlab.models.entity

import com.google.gson.annotations.SerializedName

data class DetailPeminjaman(
    @SerializedName("id") val id: Long,
    @SerializedName("alat_id") val alatId: Long,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("kondisi_awal") val kondisiAwal: String?,
    @SerializedName("kondisi_akhir") val kondisiAkhir: String?,
    @SerializedName("catatan_pengembalian") val catatanPengembalian: String?,
    @SerializedName("alat") val alat: Alat?
)

data class Peminjaman(
    @SerializedName("id") val id: Long,
    @SerializedName("kode_peminjaman") val kodePeminjaman: String,
    @SerializedName("mahasiswa_id") val mahasiswaId: Long,
    @SerializedName("tanggal_pengajuan") val tanggalPengajuan: String,
    @SerializedName("tanggal_pinjam") val tanggalPinjam: String?,
    @SerializedName("tanggal_rencana_kembali") val tanggalRencanaKembali: String?,
    @SerializedName("status") val status: String,
    @SerializedName("catatan") val catatan: String?,
    @SerializedName("detail") val detail: List<DetailPeminjaman> = emptyList(),
    @SerializedName("mahasiswa") val mahasiswa: User?
)