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
import com.example.sistempeminjamanalatlab.adapter.PeminjamanApprovalAdapter
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.inventaris.AddEditAlatActivity
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.peminjaman.DetailPinjamActivity
import com.example.sistempeminjamanalatlab.repository.PeminjamanRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.PeminjamanViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LaboranActivity : AppCompatActivity() {

    private lateinit var rvPeminjaman: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvWelcome: TextView

    // ─── REVISI 1: Tambahkan variabel untuk tombol melayang tambah alat ───
    private lateinit var fabAddAlat: FloatingActionButton

    private lateinit var adapter: PeminjamanApprovalAdapter
    private lateinit var viewModel: PeminjamanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboran)

        // ─── REVISI 2: Pastikan ID ini sama dengan yang tertulis di file lab_activity.xml ───
        rvPeminjaman = findViewById(R.id.rvPeminjaman) // Ganti jadi rv_peminjaman jika di XML pakai snake_case
        progressBar = findViewById(R.id.progressBar)
        tvWelcome = findViewById(R.id.tvWelcome)

        // Inisialisasi tombol melayangnya
        fabAddAlat = findViewById(R.id.fabTambahAlat) // Pastikan ID ini kamu pasang juga di XML lab_activity

        val name = SessionManager.getUser(this)
        tvWelcome.text = "Halo, Laboran $name!"

        // ─── REVISI 3: Set aksi klik tombol melayang untuk melompat ke form tambah alat ───
        fabAddAlat.setOnClickListener {
            val intent = Intent(this, AddEditAlatActivity::class.java)
            startActivity(intent)
        }

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
        adapter = PeminjamanApprovalAdapter(arrayListOf()) { pinjam ->
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