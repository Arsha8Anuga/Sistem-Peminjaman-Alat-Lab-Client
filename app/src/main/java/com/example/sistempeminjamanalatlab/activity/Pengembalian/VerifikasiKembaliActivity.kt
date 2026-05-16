package com.example.sistempeminjamanalatlab.activity.pengembalian

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.adapter.VerifikasiKembaliAdapter
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman
import com.example.sistempeminjamanalatlab.models.request.VerifyPengembalianRequest
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.PengembalianViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory

class VerifikasiKembaliActivity : AppCompatActivity() {

    private lateinit var tvKodePeminjaman: TextView
    private lateinit var tvNamaMahasiswa: TextView
    private lateinit var tvTanggalKembali: TextView
    private lateinit var rvDetailKembali: RecyclerView
    private lateinit var spinnerStatusVerifikasi: Spinner
    private lateinit var etDenda: EditText
    private lateinit var etCatatanVerifikasi: EditText
    private lateinit var btnVerifikasiKembali: Button
    private lateinit var progressBar: ProgressBar // Ditambahkan untuk memantau loading state

    private lateinit var viewModel: PengembalianViewModel
    private lateinit var adapter: VerifikasiKembaliAdapter

    private var token: String = ""
    private var peminjamanId: Long = -1L
    private val detailList = mutableListOf<DetailPeminjaman>()

    private val statusList = listOf("sesuai", "rusak", "hilang")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab_verifikasi_kembali_activity)

        token = SessionManager.getBearerToken(this) ?: ""
        peminjamanId = intent.getLongExtra("PEMINJAMAN_ID", -1L)

        if (token.isBlank() || peminjamanId == -1L) {
            Toast.makeText(this, "Data verifikasi tidak lengkap", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initView()
        setupSpinner()
        setupRecyclerView()
        setupViewModel()
        observeViewModel()

        // 🔴 DIUBAH: Ganti 'fetchDetailPengembalian' menjadi 'fetchDetail'
        viewModel.fetchDetail(token, peminjamanId)

        btnVerifikasiKembali.setOnClickListener {
            submitVerifikasi()
        }
    }

    private fun initView() {
        tvKodePeminjaman = findViewById(R.id.tv_kode_peminjaman)
        tvNamaMahasiswa = findViewById(R.id.tv_nama_mahasiswa)
        tvTanggalKembali = findViewById(R.id.tv_tanggal_kali) // Sesuaikan ID XML Anda
        rvDetailKembali = findViewById(R.id.rv_detail_kembali)
        spinnerStatusVerifikasi = findViewById(R.id.spinner_status_verifikasi)
        etDenda = findViewById(R.id.et_denda)
        etCatatanVerifikasi = findViewById(R.id.et_catatan_verifikasi)
        btnVerifikasiKembali = findViewById(R.id.btn_verifikasi_kembali)
        progressBar = findViewById(R.id.progressBar) // Pastikan ditambahkan ke XML
    }

    private fun setupSpinner() {
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatusVerifikasi.adapter = spinnerAdapter
    }

    private fun setupRecyclerView() {
        adapter = VerifikasiKembaliAdapter(detailList)
        rvDetailKembali.layoutManager = LinearLayoutManager(this)
        rvDetailKembali.adapter = adapter
    }

    // 3. Inisialisasi ViewModel via Factory Multi-Repository Helper
    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = com.example.sistempeminjamanalatlab.repository.PengembalianRepository(apiService)
        val factory = ViewModelFactory.getInstance(repo)
        viewModel = ViewModelProvider(this, factory).get(PengembalianViewModel::class.java)
    }

    // 4. Amati State Perubahan Data Terpusat Melalui LiveData
    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnVerifikasiKembali.isEnabled = !isLoading
        }

        viewModel.message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Pantau data respons detail pengembalian
        viewModel.detailPengembalian.observe(this) { pengembalian ->
            pengembalian?.let {
                val peminjaman = it.peminjaman

                tvKodePeminjaman.text = "Kode: ${peminjaman?.kodePeminjaman ?: "-"}"
                tvNamaMahasiswa.text = "Mahasiswa: ${peminjaman?.mahasiswa?.nama ?: "-"}"
                tvTanggalKembali.text = "Tanggal kembali: ${it.tanggalDikembalikan ?: "-"}"
                etDenda.setText(it.denda.toString())
                etCatatanVerifikasi.setText(it.catatan ?: "")

                val statusIndex = statusList.indexOf(it.statusVerifikasi)
                if (statusIndex >= 0) {
                    spinnerStatusVerifikasi.setSelection(statusIndex)
                }

                detailList.clear()
                detailList.addAll(peminjaman?.details ?: emptyList())
                adapter.notifyDataSetChanged()
            }
        }

        // Jika verifikasi dari laboran sukses, tutup halaman
        viewModel.actionSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Pengembalian berhasil diverifikasi", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun submitVerifikasi() {
        val statusVerifikasi = spinnerStatusVerifikasi.selectedItem.toString()
        val denda = etDenda.text.toString().toDoubleOrNull() ?: 0.0
        val catatan = etCatatanVerifikasi.text.toString().ifBlank { null }

        val request = VerifyPengembalianRequest(
            statusVerifikasi = statusVerifikasi,
            denda = denda,
            catatan = catatan
        )

        // 🔴 DIUBAH: Ganti 'verifyPengembalian' menjadi 'verifyReturn'
        viewModel.verifyReturn(token, peminjamanId, request)
    }
}