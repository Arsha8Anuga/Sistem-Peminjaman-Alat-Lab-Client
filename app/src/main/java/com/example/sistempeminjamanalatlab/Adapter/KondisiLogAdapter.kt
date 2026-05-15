package com.example.sistempeminjamanalatlab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.KondisiLog

class KondisiLogAdapter(
    private val listKondisiLog: List<KondisiLog>
) : RecyclerView.Adapter<KondisiLogAdapter.KondisiLogViewHolder>() {

    class KondisiLogViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val namaAlatTxt: TextView =
            itemView.findViewById(R.id.tvnamaAlat)

        val kondisiTxt: TextView =
            itemView.findViewById(R.id.kondisiTxt)

        val tanggalTxt: TextView =
            itemView.findViewById(R.id.tanggal)

        val catatanTxt: TextView =
            itemView.findViewById(R.id.catatanTxt)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KondisiLogViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.lab_kondisi_log_item,
                parent,
                false
            )

        return KondisiLogViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: KondisiLogViewHolder,
        position: Int
    ) {
        val log = listKondisiLog[position]

        holder.namaAlatTxt.text =
            "Alat ID: ${log.alatId}"

        holder.kondisiTxt.text =
            "Kondisi: ${log.kondisi}"

        holder.tanggalTxt.text =
            log.createdAt

        holder.catatanTxt.text =
            log.catatan ?: "Tidak ada catatan"
    }

    override fun getItemCount(): Int {
        return listKondisiLog.size
    }
}