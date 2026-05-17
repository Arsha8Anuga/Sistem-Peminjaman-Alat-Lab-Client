package com.example.sistempeminjamanalatlab.inventaris

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.models.*
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.Alat
import com.example.sistempeminjamanalatlab.models.entity.KategoriAlat
import com.example.sistempeminjamanalatlab.models.request.AlatCreateRequest
import com.example.sistempeminjamanalatlab.models.request.AlatUpdateRequest
import com.example.sistempeminjamanalatlab.models.request.KategoriRequest
import com.example.sistempeminjamanalatlab.repository.AlatRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.AlatViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory

class AddEditAlatActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etKode: EditText
    private lateinit var etStok: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var spinnerKategori: Spinner

    // ─── PERBAIKAN 1: Deklarasikan tombol manajemen kategori agar tidak Unresolved Reference ───
    private lateinit var btnTambahKategori: ImageButton // atau Button, sesuaikan dengan jenis view di XML
    private lateinit var btnEditKategori: ImageButton

    private lateinit var rgKondisi: RadioGroup
    private lateinit var rbBaik: RadioButton
    private lateinit var rbRusakRingan: RadioButton
    private lateinit var rbRusakBerat: RadioButton
    private lateinit var imgPreviewAlat: ImageView
    private lateinit var btnPilihFoto: Button
    private lateinit var btnSimpan: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: AlatViewModel
    private var isEditMode = false
    private var currentAlatId: Long? = null
    private var currentKategoriId: Long? = null // Menyimpan ID kategori alat yang sedang diedit
    private var selectedImagePath: String? = null
    private var kategoriList: List<KategoriAlat> = emptyList()
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imgPreviewAlat.setImageURI(uri) // Tampilkan pratinjau foto ke layar
            selectedImagePath = uri.toString() // Ambil alamat path internalnya
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab_tambah_alat_activity)

        initViews()
        setupViewModel()
        observeViewModel()

        // ─── PERBAIKAN 2: Wajib panggil fungsi ini di dalam onCreate agar tombolnya aktif! ───
        setupManajemenKategori()

        val token = SessionManager.getBearerToken(this) ?: ""
        viewModel.fetchKategori(token)

        val alatData = intent.getSerializableExtra("ALAT_DATA") as? Alat
        if (alatData != null) {
            isEditMode = true
            currentAlatId = alatData.id
            currentKategoriId = alatData.kategoriId // Simpan referensi ID kategorinya
            fillForm(alatData)
        }

        btnPilihFoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnSimpan.setOnClickListener { saveAlat() }
    }

    private fun initViews() {
        etNama = findViewById(R.id.namaAlat)
        etKode = findViewById(R.id.kodeAlat)
        etStok = findViewById(R.id.stokAlat)
        etDeskripsi = findViewById(R.id.deskripsiAlat)
        spinnerKategori = findViewById(R.id.spinnerKategori)

        // ─── PERBAIKAN 3: Hubungkan id XML tombol kategori ke variabel Kotlin ───
        btnTambahKategori = findViewById(R.id.btnTambahKategori) // Pastikan ID ini ada di XML
        btnEditKategori = findViewById(R.id.btnEditKategori)     // Pastikan ID ini ada di XML

        rgKondisi = findViewById(R.id.radioGroupKondisi)
        rbBaik = findViewById(R.id.radiobuttonbaik)
        rbRusakRingan = findViewById(R.id.radiobuttonrusakringan)
        rbRusakBerat = findViewById(R.id.radiobuttonrusakberat)
        imgPreviewAlat = findViewById(R.id.imgPreviewAlat)
        btnPilihFoto = findViewById(R.id.btnPilihFoto)
        btnSimpan = findViewById(R.id.simpanBtn)
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
            btnSimpan.isEnabled = !show
        }

        viewModel.message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        viewModel.listKategori.observe(this) { list ->
            if (list != null) {
                kategoriList = list
                val namaKategoriList = list.map { it.namaKategori ?: "Tanpa Nama" }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namaKategoriList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerKategori.adapter = adapter

                // ─── PERBAIKAN 4: Set posisi spinner pakai variabel local currentKategoriId agar lebih presisi ───
                if (isEditMode && currentKategoriId != null) {
                    val index = list.indexOfFirst { it.id == currentKategoriId }
                    if (index >= 0) spinnerKategori.setSelection(index)
                }
            }
        }

        viewModel.actionSuccess.observe(this) { success ->
            if (success) {
                viewModel.resetActionState()
                finish()
            }
        }
    }

    private fun setupManajemenKategori() {
        btnTambahKategori.setOnClickListener {
            showDialogKategori(null)
        }

        btnEditKategori.setOnClickListener {
            if (kategoriList.isNotEmpty() && spinnerKategori.selectedItemPosition != -1) {
                val kategoriDipilih = kategoriList[spinnerKategori.selectedItemPosition]
                showDialogKategori(kategoriDipilih)
            }
        }
    }

    private fun showDialogKategori(kategori: KategoriAlat?) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_kategori, null)
        val etNamaKategori = dialogView.findViewById<EditText>(R.id.et_nama_kategori)

        val token = SessionManager.getBearerToken(this) ?: ""

        if (kategori != null) {
            builder.setTitle("Edit / Hapus Kategori")
            etNamaKategori.setText(kategori.namaKategori)

            builder.setNeutralButton("Hapus") { dialog, _ ->
                viewModel.deleteKategori(token, kategori.id)
                dialog.dismiss()
            }
        } else {
            builder.setTitle("Tambah Kategori Baru")
        }

        builder.setView(dialogView)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val namaInput = etNamaKategori.text.toString()
            if (namaInput.isNotBlank()) {
                val request = KategoriRequest(namaKategori = namaInput)

                if (kategori == null) {
                    viewModel.insertKategori(token, request)
                } else {
                    viewModel.updateKategori(token, kategori.id, request)
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    private fun fillForm(alat: Alat) {
        etNama.setText(alat.namaAlat)
        etKode.setText(alat.kodeAlat)
        etStok.setText(alat.stokTotal.toString())
        etDeskripsi.setText(alat.deskripsi ?: "")

        when (alat.kondisiFisik) {
            "Baik" -> rbBaik.isChecked = true
            "Rusak Ringan" -> rbRusakRingan.isChecked = true
            "Rusak Berat" -> rbRusakBerat.isChecked = true
        }

        if (!alat.foto.isNullOrEmpty()) {
            selectedImagePath = alat.foto // simpan path lama sebagai fallback
            Glide.with(this)
                .load(alat.foto)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_report_image)
                .into(imgPreviewAlat)
        }
    }

    private fun saveAlat() {
        val token = SessionManager.getBearerToken(this) ?: ""

        if (kategoriList.isEmpty() || spinnerKategori.selectedItemPosition == -1) {
            Toast.makeText(this, "Pilih kategori terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedKategori = kategoriList[spinnerKategori.selectedItemPosition]
        val kondisi = when {
            rbBaik.isChecked -> "Baik"
            rbRusakRingan.isChecked -> "Rusak Ringan"
            else -> "Rusak Berat"
        }

        if (isEditMode && currentAlatId != null) {
            // 🟢 PERBAIKAN: Hapus parameter 'foto' dari sini karena Multipart diproses terpisah di ViewModel/Repository
            val updateRequest = AlatUpdateRequest(
                namaAlat = etNama.text.toString(),
                kodeAlat = etKode.text.toString(),
                kategoriId = selectedKategori.id,
                stokTotal = etStok.text.toString().toIntOrNull() ?: 0,
                deskripsi = etDeskripsi.text.toString(),
                kondisiFisik = kondisi,
                merk = null,
                spesifikasi = null,
                lokasiPenyimpanan = null
            )
            // Jika ViewModel kamu menggunakan Multipart terpisah, biasanya selectedImagePath dikirim sebagai argumen tambahan di sini
            viewModel.updateAlat(token, currentAlatId!!, updateRequest)
        } else {
            // 🟢 PERBAIKAN: Hapus parameter 'foto' dari sini
            val createRequest = AlatCreateRequest(
                namaAlat = etNama.text.toString(),
                kodeAlat = etKode.text.toString(),
                kategoriId = selectedKategori.id,
                stokTotal = etStok.text.toString().toIntOrNull() ?: 0,
                deskripsi = etDeskripsi.text.toString(),
                kondisiFisik = kondisi,
                merk = null,
                spesifikasi = null,
                lokasiPenyimpanan = null
            )
            viewModel.insertAlat(token, createRequest)
        }
    }
}