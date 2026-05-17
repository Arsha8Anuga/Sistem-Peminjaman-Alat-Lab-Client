package com.example.sistempeminjamanalatlab.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman

class VerifikasiKembaliAdapter(
    private var listDetail: List<DetailPeminjaman>
) : RecyclerView.Adapter<VerifikasiKembaliAdapter.VerifikasiViewHolder>() {

    // 🟢 SINKRONISASI 1: Sesuaikan ID dengan komponen TextView yang ada di XML-mu
    class VerifikasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNamaAlat: TextView = itemView.findViewById(R.id.txtNamaAlat)
        val txtKondisiAwal: TextView = itemView.findViewById(R.id.txtKondisiAwal)
        val txtKondisiAkhir: TextView = itemView.findViewById(R.id.spinnerKondisi) // Di halaman baca, spinner diwakili teks atau ID tersebut
        val txtCatatan: TextView = itemView.findViewById(R.id.etCatatan) // Menggunakan ID catatan dari XML-mu
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerifikasiViewHolder {
        // Menggunakan layout item pengembalian yang sama agar desain seragam
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_form_kembali, parent, false)
        return VerifikasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: VerifikasiViewHolder, position: Int) {
        val detail = listDetail[position]

        // 🟢 SINKRONISASI 2: Mengisi data teks ke ID yang benar sesuai XML
        holder.txtNamaAlat.text = detail.alat?.namaAlat ?: "Nama alat tidak tersedia"
        holder.txtKondisiAwal.text = "Kondisi Awal: ${detail.kondisiAwal ?: "-"}"

        // Menampilkan klaim kondisi akhir dari mahasiswa
        val kondisiAkhir = detail.kondisiAkhir ?: "Baik"
        holder.txtKondisiAkhir.text = "Kondisi Kembali: $kondisiAkhir"

        // 🎨 BONUS ESTETIKA LABORAN: Beri warna teks kondisi akhir agar laboran langsung waspada jika ada yang rusak
        when (kondisiAkhir.lowercase()) {
            "baik" -> holder.txtKondisiAkhir.setTextColor(Color.parseColor("#10B981")) // Hijau
            "rusak ringan", "rsk. ringan" -> holder.txtKondisiAkhir.setTextColor(Color.parseColor("#FF9800")) // Oranye
            "rusak berat", "rsk. berat" -> holder.txtKondisiAkhir.setTextColor(Color.parseColor("#EF4444")) // Merah
            else -> holder.txtKondisiAkhir.setTextColor(Color.BLACK)
        }

        // Menampilkan catatan alasan kerusakan dari mahasiswa
        holder.txtCatatan.text = "Catatan Mhs: ${detail.catatanPengembalian ?: "-"}"

        // Karena ini mode verifikasi (hanya membaca), pastikan kolom catatan tidak bisa diketik oleh laboran
        holder.txtCatatan.isEnabled = false
        holder.txtKondisiAkhir.isEnabled = false
    }

    override fun getItemCount(): Int = listDetail.size

    // Fungsi pembantu jika laboran melakukan refresh data dari server
    fun setData(newList: List<DetailPeminjaman>) {
        this.listDetail = newList
        notifyDataSetChanged()
    }
}