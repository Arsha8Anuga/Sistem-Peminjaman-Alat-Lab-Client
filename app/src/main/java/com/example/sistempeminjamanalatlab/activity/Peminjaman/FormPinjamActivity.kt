package com.example.sistempeminjamanalatlab.peminjaman

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sistempeminjamanalatlab.Adapter.CartAdapter
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.databinding.ActivityFormPinjamBinding
import com.example.sistempeminjamanalatlab.models.request.PeminjamanRequest
import com.example.sistempeminjamanalatlab.models.request.DetailPeminjamanRequest
import com.example.sistempeminjamanalatlab.models.response.PeminjamanResponse
import com.example.sistempeminjamanalatlab.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FormPinjamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormPinjamBinding
    private val apiService = APIClient.buildService(APIService::class.java)

    // Simulasi data dari keranjang (biasanya dilempar via Intent atau Singleton)
    private var listSelectedAlat = mutableListOf<DetailPeminjamanRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPinjamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatePickers()

        binding.btnSubmitPinjam.setOnClickListener {
            prosesPeminjaman()
        }
    }

    private fun setupDatePickers() {
        binding.etTanggalPinjam.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                binding.etTanggalPinjam.setText("$y-${m + 1}-$d")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun prosesPeminjaman() {
        val tglPinjam = binding.etTanggalPinjam.text.toString()
        val tglKembali = binding.etTanggalKembali.text.toString()
        val tujuan = binding.etTujuan.text.toString()

        if (tglPinjam.isEmpty() || tglKembali.isEmpty()) {
            Toast.makeText(this, "Lengkapi tanggal!", Toast.LENGTH_SHORT).show()
            return
        }

        val token = "Bearer YOUR_TOKEN"
        val request = PeminjamanRequest(
            tanggalPinjam = tglPinjam,
            tanggalKembali = tglKembali,
            tujuanPeminjaman = tujuan,
            items = listSelectedAlat // List alat yang sudah dipilih
        )

        apiService.createPeminjaman(token, request).enqueue(object : Callback<PeminjamanResponse> {
            override fun onResponse(call: Call<PeminjamanResponse>, response: Response<PeminjamanResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@FormPinjamActivity, "Berhasil Mengajukan!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(call: Call<PeminjamanResponse>, t: Throwable) {
                Toast.makeText(this@FormPinjamActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}