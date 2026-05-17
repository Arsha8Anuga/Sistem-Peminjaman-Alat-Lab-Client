package com.example.sistempeminjamanalatlab.activity.Pengembalian

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.adapter.FormKembaliAdapter
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman
import com.example.sistempeminjamanalatlab.models.request.DetailKembaliRequest
import com.example.sistempeminjamanalatlab.models.request.PengembalianRequest
import com.example.sistempeminjamanalatlab.repository.PengembalianRepository
import com.example.sistempeminjamanalatlab.repository.PeminjamanRepository // Tambahkan import ini
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.PengembalianViewModel
import com.example.sistempeminjamanalatlab.viewmodel.PeminjamanViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory

class LaporKembaliActivity : AppCompatActivity() {

    private lateinit var tvKodePeminjaman: TextView
    private lateinit var tvTanggalPinjam: TextView
    private lateinit var rvAlatKembali: RecyclerView
    private lateinit var btnSubmitKembali: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var formKembaliAdapter: FormKembaliAdapter

    // ✅ DEKLARASI DUA VIEWMODEL
    private lateinit var pengembalianViewModel: PengembalianViewModel
    private lateinit var peminjamanViewModel: PeminjamanViewModel

    private var token: String = ""
    private var peminjamanId: Long = -1L

    private val detailList = mutableListOf<DetailPeminjaman>()
    private val hasilKembaliMap = mutableMapOf<Long, DetailKembaliRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mhs_form_kembali_activity)

        token = SessionManager.getBearerToken(this) ?: ""
        peminjamanId = intent.getLongExtra("PEMINJAMAN_ID", -1L)

        if (token.isBlank() || peminjamanId == -1L) {
            Toast.makeText(this, "Data pengembalian tidak lengkap", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initView()
        setupViewModel()
        observeViewModel()
        setupRecyclerView()

        // ✅ Panggil fungsi fetchDetail milik PeminjamanViewModel
        peminjamanViewModel.fetchDetail(token, peminjamanId)

        btnSubmitKembali.setOnClickListener {
            submitPengembalian()
        }
    }

    private fun initView() {
        tvKodePeminjaman = findViewById(R.id.tv_kode_peminjaman)
        tvTanggalPinjam = findViewById(R.id.tv_tanggal_pinjam)
        rvAlatKembali = findViewById(R.id.rv_alat_kembali)
        btnSubmitKembali = findViewById(R.id.btn_submit_kembali)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)

        // ✅ Merakit ViewModel 1: Peminjaman
        val peminjamanRepo = PeminjamanRepository(apiService)
        val peminjamanFactory = ViewModelFactory.getInstance(peminjamanRepo)
        peminjamanViewModel = ViewModelProvider(this, peminjamanFactory).get(PeminjamanViewModel::class.java)

        // ✅ Merakit ViewModel 2: Pengembalian
        val pengembalianRepo = PengembalianRepository(apiService)
        val pengembalianFactory = ViewModelFactory.getInstance(pengembalianRepo)
        pengembalianViewModel = ViewModelProvider(this, pengembalianFactory).get(PengembalianViewModel::class.java)
    }

    private fun observeViewModel() {
        // Pantau loading dari pengembalian saat submit data
        pengembalianViewModel.isLoading.observe(this) { show ->
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            btnSubmitKembali.isEnabled = !show
        }

        pengembalianViewModel.message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // ✅ Amati data detail transaksi yang datang dari PeminjamanViewModel
        peminjamanViewModel.detailPeminjaman.observe(this) { peminjaman ->
            peminjaman?.let {
                tvKodePeminjaman.text = "Kode: ${it.kodePeminjaman}"
                tvTanggalPinjam.text = "Dipinjam pada: ${it.tanggalPinjam ?: "-"}"

                detailList.clear()
                detailList.addAll(it.details)

                it.details.forEach { detail ->
                    hasilKembaliMap[detail.id] = DetailKembaliRequest(
                        detailId = detail.id,
                        kondisiAkhir = detail.kondisiAkhir ?: "Baik",
                        catatan = detail.catatanPengembalian
                    )
                }
                formKembaliAdapter.notifyDataSetChanged()
            }
        }

        // Pantau sinyal sukses submit dari PengembalianViewModel
        pengembalianViewModel.actionSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Pengembalian berhasil diajukan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupRecyclerView() {
        formKembaliAdapter = FormKembaliAdapter(detailList) { detail, kondisiAkhir, catatan ->
            hasilKembaliMap[detail.id] = DetailKembaliRequest(
                detailId = detail.id,
                kondisiAkhir = kondisiAkhir,
                catatan = catatan.ifBlank { null }
            )
        }
        rvAlatKembali.layoutManager = LinearLayoutManager(this)
        rvAlatKembali.adapter = formKembaliAdapter
    }

    private fun submitPengembalian() {
        if (detailList.isEmpty()) {
            Toast.makeText(this, "Tidak ada alat untuk dikembalikan", Toast.LENGTH_SHORT).show()
            return
        }

        val itemsKembali = detailList.map { detail ->
            hasilKembaliMap[detail.id] ?: DetailKembaliRequest(
                detailId = detail.id,
                kondisiAkhir = "Baik",
                catatan = null
            )
        }

        val request = PengembalianRequest(
            peminjamanId = peminjamanId,
            catatan = "Pengembalian diajukan oleh mahasiswa",
            itemsKembali = itemsKembali
        )

        // ✅ Kirim data menggunakan PengembalianViewModel
        pengembalianViewModel.submitReturn(token, request)
    }
}