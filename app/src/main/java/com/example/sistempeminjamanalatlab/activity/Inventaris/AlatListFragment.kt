package com.example.sistempeminjamanalatlab.inventaris

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.Adapter.AlatAdapter
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.repository.AlatRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.AlatViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory

class AlatListFragment : Fragment() {

    // Deklarasi View Klasik
    private lateinit var rvAlat: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: AlatAdapter
    private lateinit var viewModel: AlatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Inflate layout secara manual
        val view = inflater.inflate(R.layout.fragment_alat_list, container, false)

        // 2. Inisialisasi View menggunakan findViewById dari objek 'view'
        rvAlat = view.findViewById(R.id.recyclerKondisiLog)
        progressBar = view.findViewById(R.id.progressBar) // Pastikan ID ini ada di XML

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ─── PERBAIKAN 2: Siapkan ViewModel dan Adapter (RecyclerView) dulu ───
        setupViewModel()
        setupRecyclerView() // Adapter dibuat di sini
        observeViewModel()  // Baru amati datanya di sini agar aman

        // 3. Ambil data setelah semua observer dan adapter siap menampung
        val token = SessionManager.getBearerToken(requireContext()) ?: ""
        viewModel.fetchAllAlat(token)
    }

    private fun setupRecyclerView() {
        // Tetap pertahankan GridLayoutManager 2 kolom agar rapi berbentuk grid/kotak
        rvAlat.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = AlatAdapter(arrayListOf()) { alat ->
            val intent = Intent(requireContext(), AlatDetailActivity::class.java)
            intent.putExtra("ALAT_ID", alat.id)
            startActivity(intent)
        }
        rvAlat.adapter = adapter
    }

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = AlatRepository(apiService)

        // Diubah menggunakan helper static .getInstance() agar sinkron dengan Factory baru
        val factory = ViewModelFactory.getInstance(repo)

        // ViewModelProvider menggunakan 'this' (Fragment Lifecycle) sudah tepat
        viewModel = ViewModelProvider(this, factory).get(AlatViewModel::class.java)
    }

    private fun observeViewModel() {
        // Pantau Loading State
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Pantau List Data
        viewModel.listAlat.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                adapter.setData(list)
            }
        }

        // Pantau Error Message
        viewModel.message.observe(viewLifecycleOwner) { msg ->
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}