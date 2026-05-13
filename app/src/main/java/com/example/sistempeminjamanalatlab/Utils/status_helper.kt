package com.example.sistempeminjamanalatlab.utils

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.sistempeminjamanalatlab.R

/**
 * StatusHelper
 * Mengurus semua ENUM dari database menjadi label, warna, dan ikon untuk UI.
 *
 * Penggunaan:
 *   StatusHelper.getPeminjamanLabel("pending")       → "Menunggu"
 *   StatusHelper.getPeminjamanColor("disetujui")     → R.color.status_green
 *   StatusHelper.applyPeminjamanStatus(context, tv, "pending")
 */
object StatusHelper {

    // ════════════════════════════════════════════════════
    // STATUS PEMINJAMAN
    // pending | disetujui | ditolak | dipinjam |
    // dikembalikan | selesai | dibatalkan
    // ════════════════════════════════════════════════════

    fun getPeminjamanLabel(status: String?): String = when (status) {
        "pending"       -> "Menunggu"
        "disetujui"     -> "Disetujui"
        "ditolak"       -> "Ditolak"
        "dipinjam"      -> "Sedang Dipinjam"
        "dikembalikan"  -> "Dikembalikan"
        "selesai"       -> "Selesai"
        "dibatalkan"    -> "Dibatalkan"
        else            -> "Tidak Diketahui"
    }

    fun getPeminjamanColorRes(status: String?): Int = when (status) {
        "pending"       -> R.color.status_yellow
        "disetujui"     -> R.color.status_blue
        "ditolak"       -> R.color.status_red
        "dipinjam"      -> R.color.status_orange
        "dikembalikan"  -> R.color.status_teal
        "selesai"       -> R.color.status_green
        "dibatalkan"    -> R.color.status_gray
        else            -> R.color.status_gray
    }

    fun getPeminjamanIcon(status: String?): Int = when (status) {
        "pending"       -> R.drawable.ic_clock
        "disetujui"     -> R.drawable.ic_check_circle
        "ditolak"       -> R.drawable.ic_cancel
        "dipinjam"      -> R.drawable.ic_inventory
        "dikembalikan"  -> R.drawable.ic_return
        "selesai"       -> R.drawable.ic_done_all
        "dibatalkan"    -> R.drawable.ic_block
        else            -> R.drawable.ic_help
    }

    // ════════════════════════════════════════════════════
    // KONDISI ALAT
    // baik | rusak_ringan | rusak_berat | maintenance | hilang
    // ════════════════════════════════════════════════════

    fun getKondisiLabel(kondisi: String?): String = when (kondisi) {
        "baik"          -> "Baik"
        "rusak_ringan"  -> "Rusak Ringan"
        "rusak_berat"   -> "Rusak Berat"
        "maintenance"   -> "Maintenance"
        "hilang"        -> "Hilang"
        else            -> "Tidak Diketahui"
    }

    fun getKondisiColorRes(kondisi: String?): Int = when (kondisi) {
        "baik"          -> R.color.status_green
        "rusak_ringan"  -> R.color.status_yellow
        "rusak_berat"   -> R.color.status_red
        "maintenance"   -> R.color.status_orange
        "hilang"        -> R.color.status_red
        else            -> R.color.status_gray
    }

    // ════════════════════════════════════════════════════
    // STATUS KETERSEDIAAN ALAT
    // tersedia | dipinjam | maintenance
    // ════════════════════════════════════════════════════

    fun getKetersediaanLabel(status: String?): String = when (status) {
        "tersedia"      -> "Tersedia"
        "dipinjam"      -> "Sedang Dipinjam"
        "maintenance"   -> "Maintenance"
        else            -> "Tidak Diketahui"
    }

    fun getKetersediaanColorRes(status: String?): Int = when (status) {
        "tersedia"      -> R.color.status_green
        "dipinjam"      -> R.color.status_orange
        "maintenance"   -> R.color.status_yellow
        else            -> R.color.status_gray
    }

    // ════════════════════════════════════════════════════
    // STATUS VERIFIKASI PENGEMBALIAN
    // menunggu | sesuai | rusak | hilang
    // ════════════════════════════════════════════════════

    fun getVerifikasiLabel(status: String?): String = when (status) {
        "menunggu"  -> "Menunggu Verifikasi"
        "sesuai"    -> "Sesuai"
        "rusak"     -> "Ada Kerusakan"
        "hilang"    -> "Hilang"
        else        -> "Tidak Diketahui"
    }

    fun getVerifikasiColorRes(status: String?): Int = when (status) {
        "menunggu"  -> R.color.status_yellow
        "sesuai"    -> R.color.status_green
        "rusak"     -> R.color.status_orange
        "hilang"    -> R.color.status_red
        else        -> R.color.status_gray
    }

    // ════════════════════════════════════════════════════
    // ROLE USER
    // ════════════════════════════════════════════════════

    fun getRoleLabel(role: String?): String = when (role) {
        "mahasiswa" -> "Mahasiswa"
        "laboran"   -> "Laboran"
        "asisten"   -> "Asisten"
        "admin"     -> "Admin"
        else        -> "Tidak Diketahui"
    }

    fun getRoleColorRes(role: String?): Int = when (role) {
        "mahasiswa" -> R.color.status_blue
        "laboran"   -> R.color.status_teal
        "asisten"   -> R.color.status_orange
        "admin"     -> R.color.status_red
        else        -> R.color.status_gray
    }

    // ════════════════════════════════════════════════════
    // HELPER: langsung apply ke TextView
    // ════════════════════════════════════════════════════

    /** Apply label + warna teks status peminjaman ke TextView */
    fun applyPeminjamanStatus(context: Context, textView: TextView, status: String?) {
        textView.text = getPeminjamanLabel(status)
        textView.setTextColor(ContextCompat.getColor(context, getPeminjamanColorRes(status)))
    }

    /** Apply label + warna teks kondisi alat ke TextView */
    fun applyKondisiAlat(context: Context, textView: TextView, kondisi: String?) {
        textView.text = getKondisiLabel(kondisi)
        textView.setTextColor(ContextCompat.getColor(context, getKondisiColorRes(kondisi)))
    }

    /** Apply label + warna teks ketersediaan alat ke TextView */
    fun applyKetersediaan(context: Context, textView: TextView, status: String?) {
        textView.text = getKetersediaanLabel(status)
        textView.setTextColor(ContextCompat.getColor(context, getKetersediaanColorRes(status)))
    }

    /** Apply label + warna teks verifikasi pengembalian ke TextView */
    fun applyVerifikasi(context: Context, textView: TextView, status: String?) {
        textView.text = getVerifikasiLabel(status)
        textView.setTextColor(ContextCompat.getColor(context, getVerifikasiColorRes(status)))
    }

    /** Apply label + warna teks role user ke TextView */
    fun applyRole(context: Context, textView: TextView, role: String?) {
        textView.text = getRoleLabel(role)
        textView.setTextColor(ContextCompat.getColor(context, getRoleColorRes(role)))
    }
}