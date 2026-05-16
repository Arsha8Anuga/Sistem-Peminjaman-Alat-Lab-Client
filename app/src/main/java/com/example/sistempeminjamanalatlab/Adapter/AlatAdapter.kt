package com.example.sistempeminjamanalatlab.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.Alat

class AlatAdapter(
    private var listAlat: List<Alat>,
    private val onItemClick: (Alat) -> Unit
) : RecyclerView.Adapter<AlatAdapter.AlatViewHolder>() {

    // ViewHolder menggunakan findViewById klasik
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
        holder.tvNamaAlat.text = alat.nama
        holder.tvStok.text = "Stok: ${alat.stok}"

        // Klik item untuk lihat detail
        holder.itemView.setOnClickListener { onItemClick(alat) }
    }

    override fun getItemCount(): Int = listAlat.size

    // Fungsi untuk update data jika ada perubahan
    fun setData(newList: List<Alat>) {
        this.listAlat = newList
        notifyDataSetChanged()
    }
}