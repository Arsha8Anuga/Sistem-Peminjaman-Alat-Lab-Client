package com.example.sistempeminjamanalatlab.inventaris

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistempeminjamanalatlab.databinding.MhsDetailAlatActivityBinding
import com.example.sistempeminjamanalatlab.models.response.AlatResponse
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.api.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlatDetailActivity : AppCompatActivity() {
    private lateinit var binding: MhsDetailAlatActivityBinding
    private val apiService = APIClient.buildService(APIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MhsDetailAlatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val alatId = intent.getLongExtra("ALAT_ID", -1L)
        if (alatId != -1L) {
            getDetailAlat(alatId)
        }
    }

    private fun getDetailAlat(id: Long) {
        val token = "Bearer YOUR_TOKEN_HERE"
        apiService.getAlatById(token, id).enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                if (response.isSuccessful) {
                    val alat = response.body()?.data
                    alat?.let {
                        binding.tvNamaAlat.text = it.namaAlat
                        binding.tvKode.text = it.id.toString()
                        binding.tvKondisi.text = it.kondisiFisik ?: "Tidak diketahui" ?: "-"
                        binding.tvKategori.text = it.kategori.toString() ?: "Tidak ada spesifikasi"
                        binding.tvDeskripsi.text = it.deskripsi ?: "Tidak ada spesifikasi"
                        binding.tvStok.text = "Tersedia: ${it.stokTersedia} / Total: ${it.stokTotal}"
                    }
                }
            }

            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                Toast.makeText(this@AlatDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}