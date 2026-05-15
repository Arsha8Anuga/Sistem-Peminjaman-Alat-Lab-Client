package com.example.sistempeminjamanalatlab.activity.pengembalian

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.adapter.VerifikasiKembaliAdapter
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.DetailPeminjaman
import com.example.sistempeminjamanalatlab.models.request.VerifyPengembalianRequest
import com.example.sistempeminjamanalatlab.models.response.BaseResponse
import com.example.sistempeminjamanalatlab.models.response.PengembalianResponse
import com.example.sistempeminjamanalatlab.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifikasiKembaliActivity : AppCompatActivity() {

    private lateinit var tvKodePeminjaman: TextView
    private lateinit var tvNamaMahasiswa: TextView
    private lateinit var tvTanggalKembali: TextView
    private lateinit var rvDetailKembali: RecyclerView
    private lateinit var spinnerStatusVerifikasi: Spinner
    private lateinit var etDenda: EditText
    private lateinit var etCatatanVerifikasi: EditText
    private lateinit var btnVerifikasiKembali: Button

    private lateinit var apiService: APIService
    private lateinit var adapter: VerifikasiKembaliAdapter

    private var token: String = ""
    private var peminjamanId: Long = -1L

    private val detailList = mutableListOf<DetailPeminjaman>()

    private val statusList = listOf(
        "sesuai",
        "rusak",
        "hilang"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab_verifikasi_kembali_activity)

        token = intent.getStringExtra("token") ?: ""
        peminjamanId = intent.getLongExtra("peminjaman_id", -1L)

        if (token.isBlank() || peminjamanId == -1L) {
            Toast.makeText(this, "Data verifikasi tidak lengkap", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        apiService = APIClient.buildService(APIService::class.java)

        initView()
        setupSpinner()
        setupRecyclerView()
        getDetailPengembalian()

        btnVerifikasiKembali.setOnClickListener {
            submitVerifikasi()
        }
    }

    private fun initView() {
        tvKodePeminjaman = findViewById(R.id.tv_kode_peminjaman)
        tvNamaMahasiswa = findViewById(R.id.tv_nama_mahasiswa)
        tvTanggalKembali = findViewById(R.id.tv_tanggal_kembali)
        rvDetailKembali = findViewById(R.id.rv_detail_kembali)
        spinnerStatusVerifikasi = findViewById(R.id.spinner_status_verifikasi)
        etDenda = findViewById(R.id.et_denda)
        etCatatanVerifikasi = findViewById(R.id.et_catatan_verifikasi)
        btnVerifikasiKembali = findViewById(R.id.btn_verifikasi_kembali)
    }

    private fun setupSpinner() {
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusList
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerStatusVerifikasi.adapter = spinnerAdapter
    }

    private fun setupRecyclerView() {
        adapter = VerifikasiKembaliAdapter(detailList)

        rvDetailKembali.layoutManager = LinearLayoutManager(this)
        rvDetailKembali.adapter = adapter
    }

    private fun getDetailPengembalian() {
        apiService.getDetailPengembalian(
            token = "Bearer $token",
            id = peminjamanId
        ).enqueue(object : Callback<PengembalianResponse> {

            override fun onResponse(
                call: Call<PengembalianResponse>,
                response: Response<PengembalianResponse>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val pengembalian = response.body()?.data

                    if (pengembalian == null) {
                        Toast.makeText(
                            this@VerifikasiKembaliActivity,
                            "Data pengembalian tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val peminjaman = pengembalian.peminjaman

                    tvKodePeminjaman.text =
                        "Kode: ${peminjaman?.kodePeminjaman ?: "-"}"

                    tvNamaMahasiswa.text =
                        "Mahasiswa: ${peminjaman?.mahasiswa?.nama ?: "-"}"

                    tvTanggalKembali.text =
                        "Tanggal kembali: ${pengembalian.tanggalDikembalikan}"

                    etDenda.setText(
                        pengembalian.denda.toString()
                    )

                    etCatatanVerifikasi.setText(
                        pengembalian.catatan ?: ""
                    )

                    val statusIndex =
                        statusList.indexOf(pengembalian.statusVerifikasi)

                    if (statusIndex >= 0) {
                        spinnerStatusVerifikasi.setSelection(statusIndex)
                    }

                    detailList.clear()
                    detailList.addAll(peminjaman?.details ?: emptyList())
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(
                        this@VerifikasiKembaliActivity,
                        "Gagal mengambil detail pengembalian",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<PengembalianResponse>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@VerifikasiKembaliActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun submitVerifikasi() {
        val statusVerifikasi =
            spinnerStatusVerifikasi.selectedItem.toString()

        val denda =
            etDenda.text.toString().toDoubleOrNull() ?: 0.0

        val catatan =
            etCatatanVerifikasi.text.toString().ifBlank { null }

        val request = VerifyPengembalianRequest(
            statusVerifikasi = statusVerifikasi,
            denda = denda,
            catatan = catatan
        )

        btnVerifikasiKembali.isEnabled = false

        apiService.verifyPengembalian(
            token = "Bearer $token",
            id = peminjamanId,
            request = request
        ).enqueue(object : Callback<BaseResponse> {

            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                btnVerifikasiKembali.isEnabled = true

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        this@VerifikasiKembaliActivity,
                        "Pengembalian berhasil diverifikasi",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                } else {
                    Toast.makeText(
                        this@VerifikasiKembaliActivity,
                        response.body()?.message ?: "Gagal verifikasi pengembalian",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<BaseResponse>,
                t: Throwable
            ) {
                btnVerifikasiKembali.isEnabled = true

                Toast.makeText(
                    this@VerifikasiKembaliActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}