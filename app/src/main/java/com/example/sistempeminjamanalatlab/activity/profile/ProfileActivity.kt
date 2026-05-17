/*package com.example.sistempeminjamanalatlab.activity.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.activity.auth.LoginActivity
import com.example.sistempeminjamanalatlab.network.APIClient
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.repository.UserRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.ProfileViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var imgProfile: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvRole: TextView
    private lateinit var btnChangePhoto: Button
    private lateinit var btnLogout: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: ProfileViewModel
    private val PICK_IMAGE_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initViews()
        setupViewModel()
        observeViewModel()

        // ─── AMBIL DATA DARI SESSION MANAGER LOKAL (SESUAI CATATAN VIEWMODEL) ───
        displayLocalProfileData()

        btnChangePhoto.setOnClickListener { openGallery() }
        btnLogout.setOnClickListener { handleLogout() }
    }

    private fun initViews() {
        imgProfile = findViewById(R.id.imgProfile)
        tvName = findViewById(R.id.tvNameProfile)
        tvEmail = findViewById(R.id.tvEmailProfile)
        tvRole = findViewById(R.id.tvRoleProfile)
        btnChangePhoto = findViewById(R.id.btnChangePhoto)
        btnLogout = findViewById(R.id.btnLogout)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = UserRepository(apiService)
        val factory = ViewModelFactory.getInstance(repo)
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
    }

    private fun displayLocalProfileData() {
        // 1. Ambil objek User utuh yang tersimpan di SharedPreferences via Gson
        val currentUser = SessionManager.getUser(this)

        if (currentUser != null) {
            // Tampilkan data langsung dari properti objek User
            tvName.text = currentUser.name ?: "Nama Tidak Ditemukan"
            tvEmail.text = currentUser.email ?: "Email Tidak Ditemukan"
            tvRole.text = (currentUser.role ?: "USER").uppercase()

            // Load foto profil dari properti objek User (Sesuaikan nama field: .photo atau .photoUrl)
            Glide.with(this)
                .load(currentUser.photo) // Sesuai dengan isi properti di data class User milikmu
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imgProfile)
        } else {
            // Fallback jika saveUserDetail belum pernah dipanggil (baru login saja)
            // Ambil data minimalis yang ada dari saveLoginSession
            tvName.text = "User" // Kamu bisa kosongi atau beri text default
            tvEmail.text = "-"
            tvRole.text = (SessionManager.getRole(this) ?: "USER").uppercase()
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
            btnChangePhoto.isEnabled = !it
        }

        viewModel.message.observe(this) { msg ->
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // ─── SINKRONISASI: MENGAMATI HASIL UPLOAD DARI VIEWMODEL ───
        viewModel.uploadSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Foto profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()

                // Di sini kamu bisa memperbarui URL foto baru ke SessionManager lokal jika response API-mu menyediakannya,
                // atau panggil displayLocalProfileData() kembali setelah session diupdate.

                viewModel.resetUploadState() // Reset state agar tidak trigger berulang
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let { uri -> uploadImage(uri) }
        }
    }

    private fun uploadImage(uri: Uri) {
        val token = SessionManager.getBearerToken(this) ?: ""
        val userId = SessionManager.getUserId(this)

        if (userId == -1L) {
            Toast.makeText(this, "User ID tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(getPathFromUri(uri))
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

        // Panggil fungsi updatePhoto yang sinkron dengan ProfileViewModel
        viewModel.updatePhoto(token, userId, body)
    }

    private fun handleLogout() {
        SessionManager.clearSession(this)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun getPathFromUri(uri: Uri): String {
        // Implementasi mengambil path file dari URI (Gunakan library atau FileUtils milikmu)
        return uri.path ?: ""
    }
}*/