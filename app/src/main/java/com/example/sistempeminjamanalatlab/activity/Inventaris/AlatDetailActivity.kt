package com.example.sistempeminjamanalatlab.inventaris

import android.content.Intent
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
import com.example.sistempeminjamanalatlab.peminjaman.FormPinjamActivity
import com.example.sistempeminjamanalatlab.repository.AlatRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.AlatViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory
import okhttp3.MultipartBody

class AlatDetailActivity : AppCompatActivity() {

    private lateinit var tvNamaAlat: TextView
    private lateinit var tvKode: TextView
    private lateinit var tvKondisi: TextView
    private lateinit var tvKategori: TextView
    private lateinit var tvDeskripsi: TextView
    private lateinit var tvStok: TextView
    private lateinit var btnPinjam: Button
    private lateinit var btnDeleteAlat: Button // TAMBAHAN: Deklarasi Tombol Hapus
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: AlatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mhs_detail_alat_activity)

        initViews()
        setupViewModel()
        observeViewModel()

        val alatId = intent.getLongExtra("ALAT_ID", -1L)
        if (alatId != -1L) {
            val token = SessionManager.getBearerToken(this) ?: ""
            viewModel.fetchAlatDetail(token, alatId)

            // SINKRON: Panggil setupDeleteButton di sini
            setupDeleteButton(alatId)
        } else {
            Toast.makeText(this, "ID Alat tidak valid", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Tampilkan/Sembunyikan tombol berdasarkan hak akses role
        if (SessionManager.isStaff(this)) {
            btnPinjam.visibility = View.GONE
            btnDeleteAlat.visibility = View.VISIBLE // Staff bisa melihat tombol hapus
        } else {
            btnPinjam.visibility = View.VISIBLE
            btnDeleteAlat.visibility = View.GONE // Mahasiswa tidak bisa menghapus
        }

        btnPinjam.setOnClickListener {
            handlePinjam(alatId)
        }
    }

    private fun initViews() {
        tvNamaAlat = findViewById(R.id.tvNamaAlat)
        tvKode = findViewById(R.id.tvKode)
        tvKondisi = findViewById(R.id.tvKondisi)
        tvKategori = findViewById(R.id.tvKategori)
        tvDeskripsi = findViewById(R.id.tvDeskripsi)
        tvStok = findViewById(R.id.tvStok)
        btnPinjam = findViewById(R.id.btnPinjam)
        btnDeleteAlat = findViewById(R.id.btnDeleteAlat) // TAMBAHAN: findViewById tombol hapus
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = AlatRepository(apiService)
        val factory = ViewModelFactory.getInstance(repo)
        viewModel = ViewModelProvider(this, factory).get(AlatViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { show ->
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.detailAlat.observe(this) { alat ->
            alat?.let {
                // ─── SINKRONISASI DATA CLASS DI HALAMAN DETAIL ───
                tvNamaAlat.text = it.namaAlat             // DIUBAH: Menggunakan .namaAlat
                tvKode.text = "Kode: ${it.kodeAlat}"
                tvKondisi.text = "Kondisi: ${it.kondisiFisik}"
                tvDeskripsi.text = it.deskripsi ?: "-"
                tvStok.text = "Stok Tersedia: ${it.stokTersedia} / ${it.stokTotal}" // Lebih informatif!

                val token = SessionManager.getBearerToken(this) ?: ""
                viewModel.fetchKategoriById(token, it.kategoriId)
            }
        }

        viewModel.kategoriDetail.observe(this) { kategori ->
            tvKategori.text = "Kategori: ${kategori?.namaKategori ?: "-"}"
        }

        // Observer tambahan untuk memantau status setelah sukses menghapus alat
        viewModel.actionSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Alat berhasil dihapus", Toast.LENGTH_SHORT).show()
                viewModel.resetActionState()
                finish()
            }
        }
    }

    private fun setupDeleteButton(alatId: Long) {
        btnDeleteAlat.setOnClickListener {
            val token = SessionManager.getBearerToken(this) ?: ""
            viewModel.deleteAlat(token, alatId)
        }
    }

    private fun uploadAlatImage(alatId: Long, imagePart: MultipartBody.Part) {
        val token = SessionManager.getBearerToken(this) ?: ""
        viewModel.uploadFoto(token, alatId, imagePart)
    }

    private fun handlePinjam(id: Long) {
        val intent = Intent(this, FormPinjamActivity::class.java)
        intent.putExtra("ALAT_ID", id)
        startActivity(intent)
    }
}