package com.example.sistempeminjamanalatlab.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.Peminjaman
import com.google.android.material.chip.Chip

class RiwayatAdapter(
    private val listRiwayat: List<Peminjaman>,
    private val onItemClick: (Peminjaman) -> Unit
) : RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    class RiwayatViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvKodePeminjaman: TextView =
            itemView.findViewById(R.id.tv_kode_peminjaman)

        val tvTglPengajuan: TextView =
            itemView.findViewById(R.id.tv_tgl_pengajuan)

        val chipStatus: Chip =
            itemView.findViewById(R.id.chip_status)

        val tvTglRencanaKembali: TextView =
            itemView.findViewById(R.id.tv_tgl_rencana_kembali)

        val tvJumlahAlat: TextView =
            itemView.findViewById(R.id.tv_jumlah_alat)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.mhs_riwayat_list_item,
                parent,
                false
            )

        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RiwayatViewHolder,
        position: Int
    ) {
        val peminjaman = listRiwayat[position]

        holder.tvKodePeminjaman.text =
            peminjaman.kodePeminjaman

        holder.tvTglPengajuan.text =
            "Diajukan: ${peminjaman.tanggalPengajuan}"

        holder.tvTglRencanaKembali.text =
            peminjaman.tanggalRencanaKembali ?: "-"

        holder.tvJumlahAlat.text =
            "${peminjaman.detail.size} Item"

        holder.chipStatus.text =
            peminjaman.status

        setStatusChipColor(
            holder.chipStatus,
            peminjaman.status
        )

        holder.itemView.setOnClickListener {
            onItemClick(peminjaman)
        }
    }

    private fun setStatusChipColor(
        chip: Chip,
        status: String
    ) {
        when (status.lowercase()) {

            "pending" -> {
                chip.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(
                        Color.parseColor("#F59E0B")
                    )
                chip.setTextColor(Color.WHITE)
            }

            "disetujui" -> {
                chip.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(
                        Color.parseColor("#3B82F6")
                    )
                chip.setTextColor(Color.WHITE)
            }

            "ditolak" -> {
                chip.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(
                        Color.parseColor("#EF4444")
                    )
                chip.setTextColor(Color.WHITE)
            }

            "dipinjam" -> {
                chip.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(
                        Color.parseColor("#10B981")
                    )
                chip.setTextColor(Color.WHITE)
            }

            "dikembalikan" -> {
                chip.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(
                        Color.parseColor("#6B7280")
                    )
                chip.setTextColor(Color.WHITE)
            }

            else -> {
                chip.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(
                        Color.parseColor("#41E2D2")
                    )
                chip.setTextColor(Color.WHITE)
            }
        }
    }

    override fun getItemCount(): Int {
        return listRiwayat.size
    }
}