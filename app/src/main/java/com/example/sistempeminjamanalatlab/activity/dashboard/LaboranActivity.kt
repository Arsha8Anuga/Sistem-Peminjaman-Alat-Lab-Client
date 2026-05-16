package com.example.sistempeminjamanalatlab.activity.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.adapter.PeminjamanAdapter
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.peminjaman.DetailPinjamActivity
import com.example.sistempeminjamanalatlab.repository.PeminjamanRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.PeminjamanViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory

class LaboranActivity : AppCompatActivity() {

    private lateinit var rvPeminjaman: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvWelcome: TextView
    private lateinit var adapter: PeminjamanAdapter
    private lateinit var viewModel: PeminjamanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboran)

        rvPeminjaman = findViewById(R.id.rvPeminjaman)
        progressBar = findViewById(R.id.progressBar)
        tvWelcome = findViewById(R.id.tvWelcome)

        val name = SessionManager.getUser(this)
        tvWelcome.text = "Halo, Laboran $name!"

        setupRecyclerView()
        setupViewModel()
        observeViewModel()

        fetchData()
    }

    private fun fetchData() {
        val token = SessionManager.getBearerToken(this) ?: ""
        viewModel.fetchAllPeminjaman(token)
    }

    private fun setupRecyclerView() {
        adapter = PeminjamanAdapter(arrayListOf()) { pinjam ->
            val intent = Intent(this, DetailPinjamActivity::class.java)
            intent.putExtra("PEMINJAMAN_ID", pinjam.id)
            startActivity(intent)
        }
        rvPeminjaman.layoutManager = LinearLayoutManager(this)
        rvPeminjaman.adapter = adapter
    }

    // ─── PERBAIKAN INISIALISASI VIEWMODEL ─────────────────────────────

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = PeminjamanRepository(apiService)

        // Diubah menggunakan helper static .getInstance() sesuai update ViewModelFactory kita
        val factory = ViewModelFactory.getInstance(repo)

        viewModel = ViewModelProvider(this, factory).get(PeminjamanViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.listPeminjaman.observe(this) { list ->
            if (list != null) {
                adapter.setData(list)
            }
        }

        viewModel.message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }
}