package com.example.sistempeminjamanalatlab.peminjaman

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.databinding.LabApprovalCardItemBinding
import com.example.sistempeminjamanalatlab.models.response.WrappedResponse
import com.example.sistempeminjamanalatlab.models.response.PeminjamanResponse
import com.example.sistempeminjamanalatlab.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPinjamActivity : AppCompatActivity() {
    private lateinit var binding: LabApprovalCardItemBinding
    private val apiService = APIClient.buildService(APIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LabApprovalCardItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idPinjam = intent.getLongExtra("ID_PINJAM", -1L)
        if (idPinjam != -1L) {
            getDetail(idPinjam)
        }
    }

    private fun getDetail(id: Long) {
        val token = "Bearer YOUR_TOKEN"
        apiService.getDetailPeminjaman(token, id).enqueue(object : Callback<PeminjamanResponse> {
            override fun onResponse(call: Call<PeminjamanResponse>, response: Response<PeminjamanResponse>) {
                if (response.isSuccessful) {
                    val p = response.body()?.data
                    p?.let {
                        binding.tvkodePeminjaman.text = it.kodePeminjaman
                        binding.tvstatus.text = it.status
                        binding.tanggal.text = it.tanggalPinjam
                        binding.namaPeminjam.text = it.mahasiswa?.nama

                        // Logika tombol berdasarkan status
                        if (it.status == "approved") {
                            binding.btnAksi.visibility = View.VISIBLE
                            binding.btnAksi.text = "Konfirmasi Pengambilan"
                        } else {
                            binding.btnAksi.visibility = View.GONE
                        }
                    }
                }
            }
            override fun onFailure(call: Call<PeminjamanResponse>, t: Throwable) {}
        })
    }
}