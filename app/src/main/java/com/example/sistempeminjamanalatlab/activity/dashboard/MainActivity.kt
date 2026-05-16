package com.example.sistempeminjamanalatlab.activity.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.Adapter.AlatAdapter
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.inventaris.AlatDetailActivity
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.repository.AlatRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.AlatViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var rvAlat: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: AlatAdapter
    private lateinit var viewModel: AlatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inisialisasi UI klasik
        rvAlat = findViewById(R.id.rvAlat)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
        setupViewModel()
        observeViewModel()

        // 2. Ambil data alat dari server
        val token = SessionManager.getBearerToken(this) ?: ""
        viewModel.fetchAllAlat(token)
    }

    private fun setupRecyclerView() {
        adapter = AlatAdapter(arrayListOf()) { alat ->
            // Aksi saat item diklik: Pindah ke Detail Alat
            val intent = Intent(this, AlatDetailActivity::class.java)
            intent.putExtra("ALAT_ID", alat.id)
            startActivity(intent)
        }
        rvAlat.layoutManager = LinearLayoutManager(this)
        rvAlat.adapter = adapter
    }

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = AlatRepository(apiService)

        // Diubah menggunakan helper static .getInstance() sesuai update ViewModelFactory kita
        val factory = ViewModelFactory.getInstance(repo)

        viewModel = ViewModelProvider(this, factory).get(AlatViewModel::class.java)
    }

    private fun observeViewModel() {
        // Pantau Loading
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Pantau Data Alat
        viewModel.listAlat.observe(this) { list ->
            if (list != null) {
                adapter.setData(list)
            }
        }

        // Pantau Pesan Error
        viewModel.message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}