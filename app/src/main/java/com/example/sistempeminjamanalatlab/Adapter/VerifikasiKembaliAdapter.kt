package com.example.sistempeminjamanalatlab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman

class VerifikasiKembaliAdapter(
    private val listDetail: List<DetailPeminjaman>
) : RecyclerView.Adapter<VerifikasiKembaliAdapter.VerifikasiViewHolder>() {

    class VerifikasiViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNamaAlat: TextView =
            itemView.findViewById(R.id.tv_nama_alat)

        val tvJumlah: TextView =
            itemView.findViewById(R.id.tv_jumlah)

        val tvKondisiAwal: TextView =
            itemView.findViewById(R.id.tv_kondisi_awal)

        val tvKondisiAkhir: TextView =
            itemView.findViewById(R.id.tv_kondisi_akhir)

        val tvCatatanMahasiswa: TextView =
            itemView.findViewById(R.id.tv_catatan_mahasiswa)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VerifikasiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.lab_verifikasi_kembali_item,
                parent,
                false
            )

        return VerifikasiViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: VerifikasiViewHolder,
        position: Int
    ) {
        val detail = listDetail[position]

        holder.tvNamaAlat.text =
            detail.alat?.namaAlat ?: "Nama alat tidak tersedia"

        holder.tvJumlah.text =
            "Jumlah: ${detail.jumlah}"

        holder.tvKondisiAwal.text =
            "Kondisi Awal: ${detail.kondisiAwal ?: "-"}"

        holder.tvKondisiAkhir.text =
            "Kondisi Akhir: ${detail.kondisiAkhir ?: "-"}"

        holder.tvCatatanMahasiswa.text =
            "Catatan: ${detail.catatanPengembalian ?: "-"}"
    }

    override fun getItemCount(): Int {
        return listDetail.size
    }
}