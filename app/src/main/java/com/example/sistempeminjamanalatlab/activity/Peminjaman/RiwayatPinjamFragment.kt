package com.example.sistempeminjamanalatlab.peminjaman

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.adapter.RiwayatPinjamAdapter
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.repository.PeminjamanRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.PeminjamanViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory

class RiwayatPinjamFragment : Fragment() {

    // Deklarasi View Klasik
    private lateinit var rvRiwayat: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: RiwayatPinjamAdapter // Menggunakan PeminjamanAdapter yang sudah kita buat
    private lateinit var viewModel: PeminjamanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Inflate layout fragment_riwayat_pinjam
        val view = inflater.inflate(R.layout.fragment_riwayat_pinjam, container, false)

        // 2. Inisialisasi View
        rvRiwayat = view.findViewById(R.id.rvRiwayat)
        progressBar = view.findViewById(R.id.progressBar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        observeViewModel()

        // 3. Ambil riwayat peminjaman milik user (Mahasiswa)
        fetchData()
    }

    private fun fetchData() {
        val token = SessionManager.getBearerToken(requireContext()) ?: ""
        // Gunakan fungsi fetchHistory dari ViewModel
        viewModel.fetchAllPeminjaman(token)
    }

    private fun setupRecyclerView() {
        rvRiwayat.layoutManager = LinearLayoutManager(requireContext())

        adapter = RiwayatPinjamAdapter(arrayListOf()) { pinjam ->
            val intent = Intent(requireContext(), DetailPinjamActivity::class.java)

            // PERBAIKAN 1: Ubah dari "ID_PINJAM" menjadi "PEMINJAMAN_ID" agar sinkron dengan DetailPinjamActivity
            intent.putExtra("PEMINJAMAN_ID", pinjam.id)

            startActivity(intent)
        }
        rvRiwayat.adapter = adapter
    }

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = PeminjamanRepository(apiService)

        // Diubah menggunakan helper static .getInstance() agar sinkron dengan Factory baru kita
        val factory = ViewModelFactory.getInstance(repo)

        viewModel = ViewModelProvider(this, factory).get(PeminjamanViewModel::class.java)
    }

    private fun observeViewModel() {
        // Pantau status loading
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        // Pantau perubahan data list peminjaman
        viewModel.listPeminjaman.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                adapter.setData(list)
            }
        }

        // Pantau pesan error/informasi
        viewModel.message.observe(viewLifecycleOwner) { msg ->
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Refresh data secara otomatis saat user kembali ke fragment ini
    override fun onResume() {
        super.onResume()
        fetchData()
    }
}