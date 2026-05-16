package com.example.sistempeminjamanalatlab.activity.pengembalian

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
import com.example.sistempeminjamanalatlab.Adapter.FormKembaliAdapter
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.*
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman
import com.example.sistempeminjamanalatlab.models.request.DetailKembaliRequest
import com.example.sistempeminjamanalatlab.models.request.PengembalianRequest
import com.example.sistempeminjamanalatlab.models.response.PeminjamanResponse
import com.example.sistempeminjamanalatlab.repository.PengembalianRepository // Sesuaikan nama repo pengembalianmu
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.PengembalianViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaporKembaliActivity : AppCompatActivity() {

    private lateinit var tvKodePeminjaman: TextView
    private lateinit var tvTanggalPinjam: TextView
    private lateinit var rvAlatKembali: RecyclerView
    private lateinit var btnSubmitKembali: Button
    private lateinit var progressBar: ProgressBar // TAMBAHAN: Pantau loading via ViewModel

    private lateinit var apiService: APIService
    private lateinit var formKembaliAdapter: FormKembaliAdapter

    // ─── TAMBAHAN: Hubungkan ke ViewModel agar fungsi mengambang terpakai ───
    private lateinit var viewModel: PengembalianViewModel

    private var token: String = ""
    private var peminjamanId: Long = -1L

    private val detailList = mutableListOf<DetailPeminjaman>()
    private val hasilKembaliMap = mutableMapOf<Long, DetailKembaliRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mhs_form_kembali_activity)

        // AMAN: token di sini SUDAH mengandung kata "Bearer " dari SessionManager
        token = SessionManager.getBearerToken(this) ?: ""
        peminjamanId = intent.getLongExtra("PEMINJAMAN_ID", -1L)

        if (token.isBlank() || peminjamanId == -1L) {
            Toast.makeText(this, "Data pengembalian tidak lengkap", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        apiService = APIClient.buildService(APIService::class.java)

        initView()
        setupViewModel() // TAMBAHAN: Setup komponen MVVM
        observeViewModel() // TAMBAHAN: Amati status dari ViewModel
        setupRecyclerView()
        getDetailPeminjaman()

        btnSubmitKembali.setOnClickListener {
            submitPengembalian()
        }
    }

    private fun initView() {
        tvKodePeminjaman = findViewById(R.id.tv_kode_peminjaman)
        tvTanggalPinjam = findViewById(R.id.tv_tanggal_pinjam)
        rvAlatKembali = findViewById(R.id.rv_alat_kembali)
        btnSubmitKembali = findViewById(R.id.btn_submit_kembali)
        progressBar = findViewById(R.id.progressBar) // Pastikan ID ini ada di XML mhs_form_kembali_activity
    }

    private fun setupViewModel() {
        val repo = PengembalianRepository(apiService) // Sesuaikan dengan constructor repo pengembalianmu
        val factory = ViewModelFactory.getInstance(repo)
        viewModel = ViewModelProvider(this, factory).get(PengembalianViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { show ->
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            btnSubmitKembali.isEnabled = !show
        }

        viewModel.message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // ─── SINKRONISASI: Menangkap sinyal sukses dari fungsi submitReturn() ViewModel ───
        viewModel.actionSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Pengembalian berhasil diajukan", Toast.LENGTH_SHORT).show()

                // Panggil reset state jika ada fungsi resetActionState() di PengembalianViewModel
                // viewModel.resetActionState() 

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

    private fun getDetailPeminjaman() {
        // PERBAIKAN: Gunakan variabel 'token' langsung karena sudah ber-Bearer dari SessionManager
        apiService.getDetailPeminjaman(token, peminjamanId).enqueue(object : Callback<PeminjamanResponse> {
            override fun onResponse(call: Call<PeminjamanResponse>, response: Response<PeminjamanResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val peminjaman = response.body()?.data ?: return

                    tvKodePeminjaman.text = "Kode: ${peminjaman.kodePeminjaman}"
                    tvTanggalPinjam.text = "Dipinjam pada: ${peminjaman.tanggalPinjam ?: "-"}"

                    detailList.clear()
                    detailList.addAll(peminjaman.details)

                    peminjaman.details.forEach { detail ->
                        hasilKembaliMap[detail.id] = DetailKembaliRequest(
                            detailId = detail.id,
                            kondisiAkhir = detail.kondisiAkhir ?: "Baik",
                            catatan = detail.catatanPengembalian
                        )
                    }
                    formKembaliAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@LaporKembaliActivity, "Gagal mengambil detail peminjaman", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PeminjamanResponse>, t: Throwable) {
                Toast.makeText(this@LaporKembaliActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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

        // ─── DIUBAH: Panggil fungsi submitReturn milik PengembalianViewModel ───
        viewModel.submitReturn(token, request)
    }
}