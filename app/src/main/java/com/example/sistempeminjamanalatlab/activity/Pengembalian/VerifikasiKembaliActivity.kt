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
import com.example.sistempeminjamanalatlab.repository.PengembalianRepository
import com.example.sistempeminjamanalatlab.repository.PeminjamanRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.PengembalianViewModel
import com.example.sistempeminjamanalatlab.viewmodel.PeminjamanViewModel
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
    private lateinit var progressBar: ProgressBar

    // ✅ REVISI 1: Deklarasikan kedua ViewModel secara terpisah
    private lateinit var pengembalianViewModel: PengembalianViewModel
    private lateinit var peminjamanViewModel: PeminjamanViewModel

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

        // ✅ REVISI 2: fetchDetail dipanggil dari peminjamanViewModel
        peminjamanViewModel.fetchDetail(token, peminjamanId)

        btnVerifikasiKembali.setOnClickListener {
            submitVerifikasi()
        }
    }

    private fun initView() {
        tvKodePeminjaman = findViewById(R.id.tv_kode_peminjaman)
        tvNamaMahasiswa = findViewById(R.id.tv_nama_mahasiswa)
        tvTanggalKembali = findViewById(R.id.tv_tanggal_kali)
        rvDetailKembali = findViewById(R.id.rv_detail_kembali)
        spinnerStatusVerifikasi = findViewById(R.id.spinner_status_verifikasi)
        etDenda = findViewById(R.id.et_denda)
        etCatatanVerifikasi = findViewById(R.id.et_catatan_verifikasi)
        btnVerifikasiKembali = findViewById(R.id.btn_verifikasi_kembali)
        progressBar = findViewById(R.id.progressBar)
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

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)

        // ✅ REVISI 3: Inisialisasi PeminjamanViewModel (Gunakan nama repo yang bersih)
        val peminjamanRepo = PeminjamanRepository(apiService)
        val peminjamanFactory = ViewModelFactory.getInstance(peminjamanRepo)
        peminjamanViewModel = ViewModelProvider(this, peminjamanFactory).get(PeminjamanViewModel::class.java)

        // ✅ REVISI 4: Inisialisasi PengembalianViewModel
        val pengembalianRepo = PengembalianRepository(apiService)
        val pengembalianFactory = ViewModelFactory.getInstance(pengembalianRepo)
        pengembalianViewModel = ViewModelProvider(this, pengembalianFactory).get(PengembalianViewModel::class.java)
    }

    private fun observeViewModel() {
        // Pantau loading state dari pengembalian saat menekan tombol verifikasi
        pengembalianViewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnVerifikasiKembali.isEnabled = !isLoading
        }

        pengembalianViewModel.message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // ✅ REVISI 5: Amati perubahan data detail dari peminjamanViewModel (bukan pengembalian)
        peminjamanViewModel.detailPeminjaman.observe(this) { peminjaman ->
            peminjaman?.let {
                tvKodePeminjaman.text = "Kode: ${it.kodePeminjaman}"
                tvNamaMahasiswa.text = "Mahasiswa: ${it.mahasiswa?.nama ?: "-"}"

                // Jika backend kamu punya field tanggal dikembalikan di objek peminjaman/pengembalian silakan disesuaikan
                tvTanggalKembali.text = "Tanggal Pinjam: ${it.tanggalPinjam ?: "-"}"

                detailList.clear()
                detailList.addAll(it.details)
                adapter.notifyDataSetChanged()
            }
        }

        // Jika proses eksekusi verifikasi dari laboran sukses, tutup halaman
        pengembalianViewModel.actionSuccess.observe(this) { success ->
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

        // ✅ Tetap gunakan pengembalianViewModel untuk mengirim hasil verifikasi
        pengembalianViewModel.verifyReturn(token, peminjamanId, request)
    }
}