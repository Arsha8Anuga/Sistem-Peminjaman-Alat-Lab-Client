package com.example.sistempeminjamanalatlab.peminjaman

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.request.DetailPeminjamanRequest
import com.example.sistempeminjamanalatlab.models.request.PeminjamanRequest
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.repository.PeminjamanRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.PeminjamanViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory
import java.util.*

class FormPinjamActivity : AppCompatActivity() {

    // View Klasik
    private lateinit var etTanggalPinjam: EditText
    private lateinit var etTanggalKembali: EditText
    private lateinit var etTujuan: EditText
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: PeminjamanViewModel
    private var selectedAlatId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pinjam)

        initViews()
        setupViewModel()
        observeViewModel()

        // Ambil ID alat yang dikirim dari DetailAlat (jika ada)
        selectedAlatId = intent.getLongExtra("ALAT_ID", -1L)

        setupDatePickers()

        btnSubmit.setOnClickListener {
            prosesPeminjaman()
        }
    }

    private fun initViews() {
        etTanggalPinjam = findViewById(R.id.etTanggalPinjam)
        etTanggalKembali = findViewById(R.id.etTanggalKembali)
        etTujuan = findViewById(R.id.etTujuan)
        btnSubmit = findViewById(R.id.btnSubmitPinjam)
        progressBar = findViewById(R.id.progressBar)
    }

    // ─── PERBAIKAN INISIALISASI VIEWMODEL FACTORY VIA HELPER ─────────────────────

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = PeminjamanRepository(apiService)

        // Diubah menggunakan helper static .getInstance() agar sinkron dengan Factory baru kita
        val factory = ViewModelFactory.getInstance(repo)

        viewModel = ViewModelProvider(this, factory).get(PeminjamanViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
            btnSubmit.isEnabled = !it
        }

        viewModel.message.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Jika sukses submit, tutup halaman
        viewModel.actionSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Pengajuan berhasil dikirim!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()

        val dateSetListener = { view: View, et: EditText ->
            et.setOnClickListener {
                DatePickerDialog(this, { _, y, m, d ->
                    // Format tanggal sesuai kebutuhan Backend (YYYY-MM-DD)
                    val formattedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                    et.setText(formattedDate)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        dateSetListener(etTanggalPinjam, etTanggalPinjam)
        dateSetListener(etTanggalKembali, etTanggalKembali)
    }

    private fun prosesPeminjaman() {
        // Ambil input dari form UI klasik
        val tglKembali = etTanggalKembali.text.toString()
        val catatanPeminjaman = etTujuan.text.toString() // Kita petakan input tujuan ke 'catatan'

        // Validasi input wajib
        if (tglKembali.isEmpty()) {
            Toast.makeText(this, "Harap tentukan tanggal pengembalian!", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedAlatId == -1L) {
            Toast.makeText(this, "Alat belum dipilih!", Toast.LENGTH_SHORT).show()
            return
        }

        val token = SessionManager.getBearerToken(this) ?: ""

        // 1. Membungkus item alat ke dalam list bertipe DetailPeminjamanRequest
        val detailsList = listOf(
            DetailPeminjamanRequest(
                alatId = selectedAlatId,
                jumlah = 1 // Default 1, bisa disesuaikan dengan input kuantitas jika ada
            )
        )

        // 2. Inisialisasi objek PeminjamanRequest (SEKARANG SUDAH 100% SINKRON)
        val request = PeminjamanRequest(
            tanggalRencanaKembali = tglKembali,
            catatan = catatanPeminjaman,
            details = detailsList
        )

        // 3. Kirim ke ViewModel
        viewModel.submitPeminjaman(token, request)
    }
}