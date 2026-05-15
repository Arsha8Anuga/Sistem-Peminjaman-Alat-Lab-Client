package com.example.sistempeminjamanalatlab.inventaris

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistempeminjamanalatlab.databinding.LabTambahAlatActivityBinding
import com.example.sistempeminjamanalatlab.models.entity.Alat
import com.example.sistempeminjamanalatlab.models.entity.KategoriAlat
import com.example.sistempeminjamanalatlab.models.request.AlatCreateRequest
import com.example.sistempeminjamanalatlab.models.response.AlatResponse
import com.example.sistempeminjamanalatlab.models.response.KategoriListResponse
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.api.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddEditAlatActivity : AppCompatActivity() {
    private lateinit var binding: LabTambahAlatActivityBinding
    private val apiService = APIClient.buildService(APIService::class.java)
    private var isEditMode = false
    private var currentAlatId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LabTambahAlatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cek apakah mode Edit atau Tambah
        val alatJson = intent.getSerializableExtra("ALAT_DATA") as? Alat
        if (alatJson != null) {
            isEditMode = true
            currentAlatId = alatJson.id
            fillForm(alatJson)
        }

        loadKategori()

        binding.simpanBtn.setOnClickListener {
            saveAlat()
        }
    }

    private fun loadKategori() {
        val token = "Bearer YOUR_TOKEN_HERE"
        apiService.getAllKategori(token).enqueue(object : Callback<KategoriListResponse> {
            override fun onResponse(call: Call<KategoriListResponse>, response: Response<KategoriListResponse>) {
                if (response.isSuccessful) {
                    val listKategori = response.body()?.data ?: emptyList()
                    // Menggunakan toString() di KategoriAlat untuk Spinner [cite: 6]
                    val adapter = ArrayAdapter(this@AddEditAlatActivity, android.R.layout.simple_spinner_item, listKategori)
                    binding.spinnerKategori.adapter = adapter
                }
            }
            override fun onFailure(call: Call<KategoriListResponse>, t: Throwable) {}
        })
    }

    private fun fillForm(alat: Alat) {
        binding.namaAlat.setText(alat.namaAlat)
        binding.kodeAlat.setText(alat.kodeAlat)
        binding.stokAlat.setText(alat.stokTotal.toString())
        binding.deskripsiAlat.setText(alat.deskripsi)
        binding.spinnerKategori.setSelection(alat.kategoriId.toInt())
        binding.radiobuttonbaik.isChecked = alat.kondisiFisik == "Baik"
        binding.radiobuttonrusakringan.isChecked = alat.kondisiFisik == "Rusak Ringan"
        binding.radiobuttonrusakberat.isChecked = alat.kondisiFisik == "Rusak Berat"
        binding.uploadFotoBtn.setText(alat.foto)
        // Set data lainnya...
    }

    private fun saveAlat() {
        val token = "Bearer YOUR_TOKEN_HERE"
        val request = AlatCreateRequest(
            namaAlat = binding.namaAlat.text.toString(),
            kodeAlat = binding.kodeAlat.text.toString(),
            kategoriId = (binding.spinnerKategori.selectedItem as KategoriAlat).id,
            stokTotal = binding.stokAlat.text.toString().toInt(),
            merk = null,
            spesifikasi = null,
            lokasiPenyimpanan = null,
            deskripsi = binding.deskripsiAlat.text.toString(),
            foto = null, // Sekarang parameter foto sudah dikenali jika poin 1 dilakukan
            kondisiFisik = if (binding.radiobuttonbaik.isChecked) "Baik"
            else if (binding.radiobuttonrusakringan.isChecked) "Rusak Ringan"
            else "Rusak Berat"
        )

        val call = if (isEditMode) {
            apiService.updateAlat(token, currentAlatId!!, request)
        } else {
            apiService.createAlat(token, request)
        }

        call.enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddEditAlatActivity, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddEditAlatActivity, "Gagal simpan: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                Toast.makeText(this@AddEditAlatActivity, "Koneksi Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}