package com.example.sistempeminjamanalatlab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.Peminjaman

class PeminjamanAdapter(
    private var listPeminjaman: List<Peminjaman>,
    private val onItemClick: (Peminjaman) -> Unit
) : RecyclerView.Adapter<PeminjamanAdapter.PeminjamanViewHolder>() {

    class PeminjamanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaPeminjam: TextView = view.findViewById(R.id.tvNamaPeminjam)
        val tvTglPinjam: TextView = view.findViewById(R.id.tvTglPinjam)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeminjamanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_peminjaman, parent, false)
        return PeminjamanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeminjamanViewHolder, position: Int) {
        val pinjam = listPeminjaman[position]

        holder.tvNamaPeminjam.text = "Peminjam: ${pinjam.user?.name ?: "User ID: ${pinjam.user_id}"}"
        holder.tvTglPinjam.text = "Tanggal: ${pinjam.tgl_pinjam}"
        holder.tvStatus.text = pinjam.status.uppercase()

        // Pewarnaan status sederhana
        val context = holder.itemView.context
        when (pinjam.status.lowercase()) {
            "pending" -> holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark))
            "approved" -> holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark))
            "rejected" -> holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
        }

        holder.itemView.setOnClickListener { onItemClick(pinjam) }
    }

    override fun getItemCount(): Int = listPeminjaman.size

    fun setData(newList: List<Peminjaman>) {
        this.listPeminjaman = newList
        notifyDataSetChanged()
    }
}