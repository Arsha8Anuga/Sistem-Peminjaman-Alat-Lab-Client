package com.example.sistempeminjamanalatlab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.Peminjaman

class ApprovalAdapter(
    private val listPeminjaman: List<Peminjaman>,
    private val onSetujuiClick: (Peminjaman) -> Unit,
    private val onTolakClick: (
        peminjaman: Peminjaman,
        alasan: String
    ) -> Unit
) : RecyclerView.Adapter<ApprovalAdapter.ApprovalViewHolder>() {

    class ApprovalViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val namaPeminjamTxt: TextView =
            itemView.findViewById(R.id.namaPeminjamTxt)

        val namaAlatTxt: TextView =
            itemView.findViewById(R.id.namaAlatTxt)

        val tanggalTxt: TextView =
            itemView.findViewById(R.id.tanggalTxt)

        val statusTxt: TextView =
            itemView.findViewById(R.id.statusTxt)

        val alasanTolakEdt: EditText =
            itemView.findViewById(R.id.alasanTolakEdt)

        val setujuiBtn: Button =
            itemView.findViewById(R.id.setujuiBtn)

        val tolakBtn: Button =
            itemView.findViewById(R.id.tolakBtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApprovalViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.lab_approval_card_item,
                parent,
                false
            )

        return ApprovalViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ApprovalViewHolder,
        position: Int
    ) {

        val peminjaman = listPeminjaman[position]

        holder.namaPeminjamTxt.text =
            peminjaman.mahasiswa?.nama
                ?: "Nama tidak tersedia"

        val namaAlat =
            peminjaman.detail.joinToString(", ") {
                it.alat?.namaAlat ?: "-"
            }

        holder.namaAlatTxt.text =
            "Peminjaman: $namaAlat"

        holder.tanggalTxt.text =
            peminjaman.tanggalPengajuan

        holder.statusTxt.text =
            "Status: ${peminjaman.status}"

        holder.setujuiBtn.setOnClickListener {
            onSetujuiClick(peminjaman)
        }

        holder.tolakBtn.setOnClickListener {

            val alasan =
                holder.alasanTolakEdt.text.toString()

            onTolakClick(
                peminjaman,
                alasan
            )
        }
    }

    override fun getItemCount(): Int {
        return listPeminjaman.size
    }
}