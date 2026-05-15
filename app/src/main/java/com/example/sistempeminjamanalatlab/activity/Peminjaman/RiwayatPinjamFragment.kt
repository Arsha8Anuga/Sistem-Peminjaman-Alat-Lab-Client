package com.example.sistempeminjamanalatlab.peminjaman

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sistempeminjamanalatlab.Adapter.RiwayatAdapter
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.databinding.FragmentRiwayatPinjamBinding
import com.example.sistempeminjamanalatlab.models.response.PeminjamanListResponse
import com.example.sistempeminjamanalatlab.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatPinjamFragment : Fragment(R.layout.fragment_riwayat_pinjam) {
    private lateinit var binding: FragmentRiwayatPinjamBinding
    private val apiService = APIClient.buildService(APIService::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRiwayatPinjamBinding.bind(view)

        fetchRiwayat()
    }

    private fun fetchRiwayat() {
        val token = "Bearer YOUR_TOKEN"
        apiService.getMyPeminjaman(token).enqueue(object : Callback<PeminjamanListResponse> {
            override fun onResponse(call: Call<PeminjamanListResponse>, response: Response<PeminjamanListResponse>) {
                if (response.isSuccessful) {
                    val list = response.body()?.data ?: emptyList()
                    binding.rvRiwayat.layoutManager = LinearLayoutManager(context)
                    binding.rvRiwayat.adapter = RiwayatAdapter(list) { pinjam ->
                        val intent = Intent(context, DetailPinjamActivity::class.java)
                        intent.putExtra("ID_PINJAM", pinjam.id)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<PeminjamanListResponse>, t: Throwable) {}
        })
    }
}