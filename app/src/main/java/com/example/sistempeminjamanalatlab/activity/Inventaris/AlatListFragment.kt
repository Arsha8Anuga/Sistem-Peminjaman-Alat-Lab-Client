package com.example.sistempeminjamanalatlab.inventaris

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sistempeminjamanalatlab.Adapter.AlatAdapter
import com.example.sistempeminjamanalatlab.databinding.LabLaporanKondisiActivityBinding
import com.example.sistempeminjamanalatlab.models.entity.Alat
import com.example.sistempeminjamanalatlab.models.response.AlatListResponse
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.api.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlatListFragment : Fragment() {
    private var _binding: LabLaporanKondisiActivityBinding? = null
    private val binding get() = _binding!!
    private val apiService = APIClient.buildService(APIService::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LabLaporanKondisiActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchAlatData()
    }

    private fun setupRecyclerView() {
        binding.recyclerKondisiLog.layoutManager = GridLayoutManager(requireContext(), 2) // Grid layout seperti AlatAdapter [cite: 25]
    }

    private fun fetchAlatData() {
        val token = "Bearer YOUR_TOKEN_HERE"
        apiService.getAllAlat(token).enqueue(object : Callback<AlatListResponse> {
            override fun onResponse(call: Call<AlatListResponse>, response: Response<AlatListResponse>) {
                if (response.isSuccessful) {
                    val listAlat = response.body()?.data ?: emptyList()
                    binding.recyclerKondisiLog.adapter = AlatAdapter(listAlat) { alat ->
                        val intent = Intent(requireContext(), AlatDetailActivity::class.java)
                        intent.putExtra("ALAT_ID", alat.id)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<AlatListResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}