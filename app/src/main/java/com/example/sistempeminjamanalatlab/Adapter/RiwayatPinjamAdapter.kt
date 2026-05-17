package com.example.sistempeminjamanalatlab.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.Peminjaman

class RiwayatPinjamAdapter(
    private var listRiwayat: List<Peminjaman>,
    private val onItemClick: (Peminjaman) -> Unit
) : RecyclerView.Adapter<RiwayatPinjamAdapter.RiwayatViewHolder>() {

    // 🟢 SINKRONISASI 1: ViewHolder disesuaikan dengan ID asli di file XML-mu
    class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaPeminjam: TextView = itemView.findViewById(R.id.tvNamaPeminjam)
        val tvTglPinjam: TextView = itemView.findViewById(R.id.tvTglPinjam)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus) // Ubah Chip jadi TextView sesuai XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        // Pastikan nama layout di bawah ini sesuai dengan nama file XML item-mu
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_peminjaman, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val peminjaman = listRiwayat[position]

        // 🟢 SINKRONISASI 2: Mengisi data teks ke ID yang benar
        // Karena ini di sisi mahasiswa, kita bisa tampilkan Kode Peminjaman atau nama mereka
        holder.tvNamaPeminjam.text = "Nota: ${peminjaman.kodePeminjaman}"

        // Mengisi tanggal pinjam (Gunakan fallback jika null)
        holder.tvTglPinjam.text = "Tanggal: ${peminjaman.tanggalPinjam ?: peminjaman.tanggalPengajuan ?: "-"}"

        // Mengatur status teks (Kapital penuh agar rapi)
        holder.tvStatus.text = peminjaman.status.uppercase()

        // 🟢 SINKRONISASI 3: Mengubah warna background TextView secara dinamis menggantikan Chip
        applyStatusStyle(holder.tvStatus, peminjaman.status)

        holder.itemView.setOnClickListener {
            onItemClick(peminjaman)
        }
    }

    /**
     * Mengatur warna teks dan background box status agar terlihat estetik seperti Chip/Badge
     */
    private fun applyStatusStyle(textView: TextView, status: String) {
        val colorHex = when (status.lowercase()) {
            "pending" -> "#F59E0B"      // Oranye
            "approved", "disetujui" -> "#3B82F6"  // Biru
            "rejected", "ditolak" -> "#EF4444"    // Merah
            "taken", "dipinjam" -> "#10B981"      // Hijau
            "completed", "dikembalikan" -> "#6B7280" // Abu-abu
            else -> "#00BCD4"                     // Cyan (Default)
        }

        val parsedColor = Color.parseColor(colorHex)

        // Buat background melengkung halus (radius 8dp) secara programmatif tanpa mengotori berkas drawable
        val shape = GradientDrawable().apply {
            cornerRadius = 16f
            setColor(parsedColor and 0x15FFFFFF) // Set warna transparan tipis (15% opacity) sebagai latar belakang
        }

        textView.background = shape
        textView.setTextColor(parsedColor) // Warna teks solid tajam sesuai status
    }

    override fun getItemCount(): Int = listRiwayat.size

    // Tambahkan fungsi setData agar ViewModel bisa memperbarui list saat refresh/load data baru
    fun setData(newList: List<Peminjaman>) {
        this.listRiwayat = newList
        notifyDataSetChanged()
    }
}