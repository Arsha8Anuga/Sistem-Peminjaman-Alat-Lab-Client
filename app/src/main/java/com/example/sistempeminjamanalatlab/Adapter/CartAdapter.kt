package com.example.sistempeminjamanalatlab.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman

class CartAdapter(
    private val listCart: List<DetailPeminjaman>,
    private val onDeleteClick: (DetailPeminjaman) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imgCart: ImageView =
            itemView.findViewById(R.id.imgCart)

        val tvNamaAlat: TextView =
            itemView.findViewById(R.id.tvNamaAlat)

        val tvKategori: TextView =
            itemView.findViewById(R.id.tvKategori)

        val tvJumlah: TextView =
            itemView.findViewById(R.id.tvJumlah)

        val btnDelete: ImageButton =
            itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.mhs_cart_list_item,
                parent,
                false
            )

        return CartViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {

        val item = listCart[position]

        holder.tvNamaAlat.text =
            item.alat?.namaAlat ?: "Nama alat tidak tersedia"

        holder.tvKategori.text =
            item.alat?.kategori?.namaKategori ?: "Tanpa kategori"

        holder.tvJumlah.text =
            "Jumlah : ${item.jumlah}"

        holder.imgCart.setImageResource(
            R.mipmap.ic_launcher
        )

        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }

    override fun getItemCount(): Int {
        return listCart.size
    }
}