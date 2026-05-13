package com.example.sistempeminjamanalatlab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.Alat

class MasterAlatAdapter(
    private val listAlat: List<Alat>,
    private val onEditClick: (Alat) -> Unit,
    private val onDeleteClick: (Alat) -> Unit
) : RecyclerView.Adapter<MasterAlatAdapter.MasterAlatViewHolder>() {

    class MasterAlatViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNamaAlat: TextView =
            itemView.findViewById(R.id.tvNamaAlat)

        val tvKodeAlat: TextView =
            itemView.findViewById(R.id.tvKodeAlat)

        val tvKategori: TextView =
            itemView.findViewById(R.id.tvKategori)

        val tvStok: TextView =
            itemView.findViewById(R.id.tvStok)

        val tvKondisi: TextView =
            itemView.findViewById(R.id.tvKondisi)

        val btnEdit: Button =
            itemView.findViewById(R.id.btnEdit)

        val btnDelete: Button =
            itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MasterAlatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.lab_master_alat_item,
                parent,
                false
            )

        return MasterAlatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MasterAlatViewHolder,
        position: Int
    ) {
        val alat = listAlat[position]

        holder.tvNamaAlat.text = alat.namaAlat
        holder.tvKodeAlat.text = "Kode: ${alat.kodeAlat}"
        holder.tvKategori.text = "Kategori: ${alat.kategori?.namaKategori ?: "Tanpa kategori"}"
        holder.tvStok.text = "Stok: ${alat.stokTersedia}/${alat.stokTotal}"
        holder.tvKondisi.text = "Kondisi: ${alat.kondisiFisik}"

        holder.btnEdit.setOnClickListener {
            onEditClick(alat)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(alat)
        }
    }

    override fun getItemCount(): Int {
        return listAlat.size
    }
}