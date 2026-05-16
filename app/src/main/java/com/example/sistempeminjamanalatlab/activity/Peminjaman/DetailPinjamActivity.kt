package com.example.sistempeminjamanalatlab.peminjaman

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.repository.PeminjamanRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.PeminjamanViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory

class DetailPinjamActivity : AppCompatActivity() {

    // View Klasik
    private lateinit var tvKode: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvTanggal: TextView
    private lateinit var tvNamaPeminjam: TextView
    private lateinit var btnApprove: Button
    private lateinit var btnReject: Button
    private lateinit var btnAmbilAlat: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: PeminjamanViewModel
    private var currentLoanId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab_approval_card_item)

        initViews()
        setupViewModel()
        observeViewModel()

        // PERBAIKAN 1: Samakan kunci intent menjadi "PEMINJAMAN_ID" sesuai LaboranActivity
        currentLoanId = intent.getLongExtra("PEMINJAMAN_ID", -1L)
        val token = SessionManager.getBearerToken(this) ?: ""

        if (currentLoanId != -1L) {
            viewModel.fetchDetail(token, currentLoanId)
        }

        // PERBAIKAN 2: Alihkan listener ke fungsi yang tepat sesuai Role (Staff vs Mahasiswa)
        btnApprove.setOnClickListener { viewModel.approve(token, currentLoanId) }
        btnAmbilAlat.setOnClickListener { viewModel.markAsTaken(token, currentLoanId) }

        btnReject.setOnClickListener {
            if (SessionManager.isStaff(this)) {
                viewModel.reject(token, currentLoanId) // Jika laboran, status menjadi Rejected
            } else {
                viewModel.cancelLoan(token, currentLoanId) // Jika mahasiswa, panggil fungsi cancelLoan
            }
        }
    }

    private fun initViews() {
        tvKode = findViewById(R.id.tvkodePeminjaman)
        tvStatus = findViewById(R.id.tvstatus)
        tvTanggal = findViewById(R.id.tanggal)
        tvNamaPeminjam = findViewById(R.id.namaPeminjam)
        btnApprove = findViewById(R.id.btnApprove) // Tambahkan ID ini di XML
        btnReject = findViewById(R.id.btnReject)   // Tambahkan ID ini di XML
        btnAmbilAlat = findViewById(R.id.btnAmbilAlat) // Tombol "Konfirmasi Pengambilan"
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = PeminjamanRepository(apiService)

        // Diubah menggunakan helper static .getInstance() agar sinkron dengan Factory baru
        val factory = ViewModelFactory.getInstance(repo)

        viewModel = ViewModelProvider(this, factory).get(PeminjamanViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.detailPeminjaman.observe(this) { p ->
            p?.let {
                tvKode.text = it.kodePeminjaman
                tvStatus.text = it.status.uppercase()
                tvTanggal.text = it.tanggalPinjam
                tvNamaPeminjam.text = it.mahasiswa?.nama ?: "User ID: ${it.userId}"

                setupActionButtons(it.status)
            }
        }

        // Jika reject sukses, kembali ke daftar
        viewModel.actionSuccess.observe(this) { success ->
            if (success) finish()
        }
    }

    /**
     * Logika Penyeleksian Role dan Status untuk Tombol Aksi
     */
    private fun setupActionButtons(status: String) {
        val isStaff = SessionManager.isStaff(this)

        // Sembunyikan semua tombol dulu
        btnApprove.visibility = View.GONE
        btnReject.visibility = View.GONE
        btnAmbilAlat.visibility = View.GONE

        if (isStaff) {
            when (status.lowercase()) {
                "pending" -> {
                    btnApprove.visibility = View.VISIBLE
                    btnReject.visibility = View.VISIBLE
                }
                "approved" -> {
                    // Tombol Konfirmasi Pengambilan Alat Fisik
                    btnAmbilAlat.visibility = View.VISIBLE
                    btnAmbilAlat.text = "Konfirmasi Pengambilan Alat"
                }
            }
        } else {
            // Logika untuk Mahasiswa jika diperlukan (misal: Tombol Batalkan)
            if (status.lowercase() == "pending") {
                btnReject.visibility = View.VISIBLE
                btnReject.text = "Batalkan Pengajuan"
            }
        }
    }
}