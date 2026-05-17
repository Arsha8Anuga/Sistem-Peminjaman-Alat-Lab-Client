package com.example.sistempeminjamanalatlab.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.Alat

class AlatAdapter(
    private var listAlat: List<Alat>,
    private val onItemClick: (Alat) -> Unit
) : RecyclerView.Adapter<AlatAdapter.AlatViewHolder>() {

    class AlatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaAlat: TextView = view.findViewById(R.id.tvNamaAlat)
        val tvStok: TextView = view.findViewById(R.id.tvStok)
        val imgAlat: ImageView = view.findViewById(R.id.imgAlat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alat, parent, false)
        return AlatViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlatViewHolder, position: Int) {
        val alat = listAlat[position]

        // 🟢 DISESUAIKAN: Menggunakan 'namaAlat' sesuai entity kamu
        holder.tvNamaAlat.text = alat.namaAlat

        // 🟢 DISESUAIKAN: Menggunakan 'stokTersedia' sesuai entity kamu
        holder.tvStok.text = "Tersedia: ${alat.stokTersedia}"

        if (!alat.foto.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(alat.foto) // Glide otomatis mendeteksi apakah ini URL web atau File Path internal
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.imgAlat)
        } else {
            holder.imgAlat.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        // Klik item untuk lihat detail
        holder.itemView.setOnClickListener { onItemClick(alat) }
    }

    override fun getItemCount(): Int = listAlat.size

    // Fungsi untuk update data dinamis
    fun setData(newList: List<Alat>?) {
        if (newList != null) {
            this.listAlat = newList
            notifyDataSetChanged()
        }
    }
}