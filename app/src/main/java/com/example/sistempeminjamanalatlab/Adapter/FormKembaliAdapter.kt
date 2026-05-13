package com.example.sistempeminjamanalatlab.Adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman
import com.google.android.material.textfield.TextInputEditText

class FormKembaliAdapter(
    private val listDetail: List<DetailPeminjaman>,
    private val onKondisiChanged: (
        detail: DetailPeminjaman,
        kondisiAkhir: String,
        catatan: String
    ) -> Unit
) : RecyclerView.Adapter<FormKembaliAdapter.FormKembaliViewHolder>() {

    inner class FormKembaliViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNamaAlat: TextView =
            itemView.findViewById(R.id.tv_nama_alat)

        val tvKondisiAwal: TextView =
            itemView.findViewById(R.id.tv_kondisi_awal)

        val rgKondisiAkhir: RadioGroup =
            itemView.findViewById(R.id.rg_kondisi_akhir)

        val rbBaik: RadioButton =
            itemView.findViewById(R.id.rb_baik)

        val rbRusakRingan: RadioButton =
            itemView.findViewById(R.id.rb_rusak_ringan)

        val rbRusakBerat: RadioButton =
            itemView.findViewById(R.id.rb_rusak_berat)

        val etCatatanKembali: TextInputEditText =
            itemView.findViewById(R.id.et_catatan_kembali)

        var textWatcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FormKembaliViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.mhs_alat_kembali_item,
                parent,
                false
            )

        return FormKembaliViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FormKembaliViewHolder,
        position: Int
    ) {
        val detail = listDetail[position]

        holder.tvNamaAlat.text =
            detail.alat?.namaAlat ?: "Nama alat tidak tersedia"

        holder.tvKondisiAwal.text =
            "Kondisi Awal: ${detail.kondisiAwal ?: "-"}"

        holder.rgKondisiAkhir.setOnCheckedChangeListener(null)

        holder.textWatcher?.let {
            holder.etCatatanKembali.removeTextChangedListener(it)
        }

        val kondisiAwal =
            detail.kondisiAkhir ?: "Baik"

        val catatanAwal =
            detail.catatanPengembalian ?: ""

        when (kondisiAwal.lowercase()) {
            "baik" -> holder.rbBaik.isChecked = true
            "rusak ringan", "rsk. ringan" -> holder.rbRusakRingan.isChecked = true
            "rusak berat", "rsk. berat" -> holder.rbRusakBerat.isChecked = true
            else -> holder.rbBaik.isChecked = true
        }

        holder.etCatatanKembali.setText(catatanAwal)

        holder.rgKondisiAkhir.setOnCheckedChangeListener { _, checkedId ->
            val kondisiAkhir = when (checkedId) {
                R.id.rb_baik -> "Baik"
                R.id.rb_rusak_ringan -> "Rusak Ringan"
                R.id.rb_rusak_berat -> "Rusak Berat"
                else -> "Baik"
            }

            val catatan =
                holder.etCatatanKembali.text?.toString() ?: ""

            onKondisiChanged(
                detail,
                kondisiAkhir,
                catatan
            )
        }

        holder.textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val kondisiAkhir = when (holder.rgKondisiAkhir.checkedRadioButtonId) {
                    R.id.rb_baik -> "Baik"
                    R.id.rb_rusak_ringan -> "Rusak Ringan"
                    R.id.rb_rusak_berat -> "Rusak Berat"
                    else -> "Baik"
                }

                onKondisiChanged(
                    detail,
                    kondisiAkhir,
                    s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        holder.etCatatanKembali.addTextChangedListener(
            holder.textWatcher
        )
    }

    override fun getItemCount(): Int {
        return listDetail.size
    }
}