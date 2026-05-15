package com.example.sistempeminjamanalatlab.activity.pengembalian

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.Adapter.FormKembaliAdapter
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman
import com.example.sistempeminjamanalatlab.models.request.DetailKembaliRequest
import com.example.sistempeminjamanalatlab.models.request.PengembalianRequest
import com.example.sistempeminjamanalatlab.models.response.PeminjamanResponse
import com.example.sistempeminjamanalatlab.models.response.PengembalianResponse
import com.example.sistempeminjamanalatlab.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaporKembaliActivity : AppCompatActivity() {

    private lateinit var tvKodePeminjaman: TextView
    private lateinit var tvTanggalPinjam: TextView
    private lateinit var rvAlatKembali: RecyclerView
    private lateinit var btnSubmitKembali: Button

    private lateinit var apiService: APIService
    private lateinit var formKembaliAdapter: FormKembaliAdapter

    private var token: String = ""
    private var peminjamanId: Long = -1L

    private val detailList = mutableListOf<DetailPeminjaman>()
    private val hasilKembaliMap = mutableMapOf<Long, DetailKembaliRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mhs_form_kembali_activity)

        token = intent.getStringExtra("token") ?: ""
        peminjamanId = intent.getLongExtra("peminjaman_id", -1L)

        if (token.isBlank() || peminjamanId == -1L) {
            Toast.makeText(this, "Data pengembalian tidak lengkap", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        apiService = APIClient.buildService(APIService::class.java)

        initView()
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
    }

    private fun setupRecyclerView() {
        formKembaliAdapter = FormKembaliAdapter(
            detailList
        ) { detail: DetailPeminjaman, kondisiAkhir: String, catatan: String ->

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
        apiService.getDetailPeminjaman(
            token = "Bearer $token",
            id = peminjamanId
        ).enqueue(object : Callback<PeminjamanResponse> {

            override fun onResponse(
                call: Call<PeminjamanResponse>,
                response: Response<PeminjamanResponse>
            ) {
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
                    Toast.makeText(
                        this@LaporKembaliActivity,
                        "Gagal mengambil detail peminjaman",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PeminjamanResponse>, t: Throwable) {
                Toast.makeText(
                    this@LaporKembaliActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
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

        btnSubmitKembali.isEnabled = false

        apiService.createPengembalian(
            token = "Bearer $token",
            request = request
        ).enqueue(object : Callback<PengembalianResponse> {

            override fun onResponse(
                call: Call<PengembalianResponse>,
                response: Response<PengembalianResponse>
            ) {
                btnSubmitKembali.isEnabled = true

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        this@LaporKembaliActivity,
                        "Pengembalian berhasil diajukan",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@LaporKembaliActivity,
                        response.body()?.message ?: "Gagal mengajukan pengembalian",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PengembalianResponse>, t: Throwable) {
                btnSubmitKembali.isEnabled = true

                Toast.makeText(
                    this@LaporKembaliActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}