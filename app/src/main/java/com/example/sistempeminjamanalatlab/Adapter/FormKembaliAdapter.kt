package com.example.sistempeminjamanalatlab.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman

class FormKembaliAdapter(
    private val listDetail: List<DetailPeminjaman>,
    private val onKondisiChanged: (
        detail: DetailPeminjaman,
        kondisiAkhir: String,
        catatan: String
    ) -> Unit
) : RecyclerView.Adapter<FormKembaliAdapter.FormKembaliViewHolder>() {

    // 🟢 SINKRONISASI 1: ViewHolder disesuaikan dengan ID dan komponen asli XML
    inner class FormKembaliViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNamaAlat: TextView = itemView.findViewById(R.id.txtNamaAlat)
        val txtKondisiAwal: TextView = itemView.findViewById(R.id.txtKondisiAwal)
        val spinnerKondisi: Spinner = itemView.findViewById(R.id.spinnerKondisi)
        val etCatatan: EditText = itemView.findViewById(R.id.etCatatan)
        var textWatcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormKembaliViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_form_kembali, parent, false) // Pastikan nama file XML-mu sesuai
        return FormKembaliViewHolder(view)
    }

    override fun onBindViewHolder(holder: FormKembaliViewHolder, position: Int) {
        val detail = listDetail[position]

        // 🟢 SINKRONISASI 2: Ikat data ke ID teks yang benar
        holder.txtNamaAlat.text = detail.alat?.namaAlat ?: "Nama alat tidak tersedia"
        holder.txtKondisiAwal.text = "Kondisi Awal: ${detail.kondisiAwal ?: "-"}"

        // Reset listener lama agar tidak terjadi penumpukan data saat di-scroll (Recycled)
        holder.spinnerKondisi.onItemSelectedListener = null
        holder.textWatcher?.let { holder.etCatatan.removeTextChangedListener(it) }

        // 🟢 SINKRONISASI 3: Pasang data pilihan ke Spinner
        val opsiKondisi = arrayOf("Baik", "Rusak Ringan", "Rusak Berat")
        val spinnerAdapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, opsiKondisi)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinnerKondisi.adapter = spinnerAdapter

        // Set posisi default spinner berdasarkan data kondisiAkhir yang sudah ada
        val kondisiSaatIni = detail.kondisiAkhir ?: "Baik"
        val defaultPosition = when (kondisiSaatIni.lowercase()) {
            "baik" -> 0
            "rusak ringan", "rsk. ringan" -> 1
            "rusak berat", "rsk. berat" -> 2
            else -> 0
        }
        holder.spinnerKondisi.setSelection(defaultPosition)

        // Set teks catatan awal
        holder.etCatatan.setText(detail.catatanPengembalian ?: "")

        // 🟢 SINKRONISASI 4: Deteksi perubahan pilihan pada Spinner
        holder.spinnerKondisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val kondisiAkhir = opsiKondisi[pos]
                val catatan = holder.etCatatan.text.toString()

                // Update ke objek data lokal agar datanya tidak hilang saat recycler view di-scroll
                detail.kondisiAkhir = kondisiAkhir
                onKondisiChanged(detail, kondisiAkhir, catatan)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 🟢 SINKRONISASI 5: Deteksi perubahan teks pada EditText Catatan
        holder.textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val kondisiAkhir = holder.spinnerKondisi.selectedItem.toString()
                val catatanInput = s?.toString() ?: ""

                // Update ke objek data lokal
                detail.catatanPengembalian = catatanInput
                onKondisiChanged(detail, kondisiAkhir, catatanInput)
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        holder.etCatatan.addTextChangedListener(holder.textWatcher)
    }

    override fun getItemCount(): Int = listDetail.size
}