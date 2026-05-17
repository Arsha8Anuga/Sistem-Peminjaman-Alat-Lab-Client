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

class PeminjamanApprovalAdapter(
    private var listPeminjaman: List<Peminjaman>,
    private val onItemClick: (Peminjaman) -> Unit // 🟢 DISESUAIKAN: Cukup deteksi klik kartu untuk buka DetailPinjamActivity
) : RecyclerView.Adapter<PeminjamanApprovalAdapter.ApprovalViewHolder>() {

    // 🟢 SINKRONISASI 1: Hubungkan ke ID asli yang ada di dalam XML kartu peminjamanmu
    class ApprovalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaPeminjam: TextView = itemView.findViewById(R.id.tvNamaPeminjam)
        val tvTglPinjam: TextView = itemView.findViewById(R.id.tvTglPinjam)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApprovalViewHolder {
        // Menggunakan layout item kartu peminjaman yang sama agar seragam
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_peminjaman, parent, false)
        return ApprovalViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApprovalViewHolder, position: Int) {
        val peminjaman = listPeminjaman[position]

        // 🟢 SINKRONISASI 2: Karena ini di sisi Laboran, tampilkan nama mahasiswanya
        val namaMhs = peminjaman.mahasiswa?.nama ?: "Mahasiswa Tanpa Nama"
        holder.tvNamaPeminjam.text = "Peminjam: $namaMhs"

        // Menampilkan tanggal pengajuan pinjam
        holder.tvTglPinjam.text = "Tanggal: ${peminjaman.tanggalPengajuan ?: "-"}"

        // Menampilkan status nota (misal: PENDING, APPROVED)
        holder.tvStatus.text = peminjaman.status.uppercase()

        // Memberikan gaya warna badge background transparan estetik pada status TextView
        applyStatusStyle(holder.tvStatus, peminjaman.status)

        // 🟢 SINKRONISASI 3: Saat kartu diklik, lempar objek peminjaman ke LaboranActivity untuk diproses ke Detail
        holder.itemView.setOnClickListener {
            onItemClick(peminjaman)
        }
    }

    override fun getItemCount(): Int = listPeminjaman.size

    /**
     * Memperbarui daftar antrean secara dinamis saat laboran melakukan swipe-to-refresh
     */
    fun setData(newList: List<Peminjaman>) {
        this.listPeminjaman = newList
        notifyDataSetChanged()
    }

    /**
     * Menghapus item dari daftar antrean secara halus saat sudah di-approve/reject
     */
    fun removeItem(peminjamanId: Long) {
        val index = listPeminjaman.indexOfFirst { it.id == peminjamanId }
        if (index != -1) {
            // Memastikan list bisa dimanipulasi dengan aman
            val mutableList = listPeminjaman.toMutableList()
            mutableList.removeAt(index)
            this.listPeminjaman = mutableList
            notifyItemRemoved(index)
        }
    }

    private fun applyStatusStyle(textView: TextView, status: String) {
        val colorHex = when (status.lowercase()) {
            "pending" -> "#F59E0B"      // Oranye
            "approved", "disetujui" -> "#3B82F6"  // Biru
            "rejected", "ditolak" -> "#EF4444"    // Merah
            "taken", "dipinjam" -> "#10B981"      // Hijau
            else -> "#6B7280"                     // Abu-abu
        }
        val parsedColor = Color.parseColor(colorHex)
        val shape = GradientDrawable().apply {
            cornerRadius = 16f
            setColor(parsedColor and 0x15FFFFFF) // Background transparan 15%
        }
        textView.background = shape
        textView.setTextColor(parsedColor)
    }
}