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
    private val listAlat: List<Alat>,
    private val onItemClick: (Alat) -> Unit
) : RecyclerView.Adapter<AlatAdapter.AlatViewHolder>() {

    inner class AlatViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imgAlat: ImageView =
            itemView.findViewById(R.id.imgAlat)

        val tvNamaAlat: TextView =
            itemView.findViewById(R.id.tvNamaAlat)

        val tvKategori: TextView =
            itemView.findViewById(R.id.tvKategori)

        val tvStok: TextView =
            itemView.findViewById(R.id.tvStok)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mhs_alat_grid_item, parent, false)

        return AlatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AlatViewHolder,
        position: Int
    ) {
        val alat = listAlat[position]

        holder.tvNamaAlat.text = alat.namaAlat
        holder.tvKategori.text = alat.kategori?.namaKategori ?: "Tanpa Kategori"
        holder.tvStok.text = "Stock : ${alat.stokTersedia}"

        holder.imgAlat.setImageResource(R.mipmap.ic_launcher)

        holder.itemView.setOnClickListener {
            onItemClick(alat)
        }
    }

    override fun getItemCount(): Int {
        return listAlat.size
    }
}