package com.example.sistempeminjamanalatlab.activity.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.activity.dashboard.LaboranActivity
import com.example.sistempeminjamanalatlab.activity.dashboard.MainActivity
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.request.LoginRequest
import com.example.sistempeminjamanalatlab.repository.AuthRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import com.example.sistempeminjamanalatlab.viewmodel.AuthViewModel
import com.example.sistempeminjamanalatlab.viewmodel.ViewModelFactory
import com.example.sistempeminjamanalatlab.network.APIClient

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)

        setupViewModel()
        observeViewModel()

        btnLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun setupViewModel() {
        val apiService = APIClient.buildService(APIService::class.java)
        val repo = AuthRepository(apiService)
        // Disesuaikan dengan update ViewModelFactory kita kemarin yang menggunakan static helper .getInstance()
        val factory = ViewModelFactory.getInstance(repo)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnLogin.isEnabled = !isLoading
        }

        viewModel.message.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Pindahkan proses penyimpanan sesi ke sini saat loginData terisi sukses
        viewModel.loginData.observe(this) { data ->
            if (data != null) {
                SessionManager.saveLoginSession(this, data)
            }
        }

        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                navigateToDashboard()
                viewModel.resetLoginState() // Reset state sukses agar tidak memicu navigasi berulang
            }
        }
    }

    private fun handleLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email/Password kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LoginRequest(email, password)
        // Diubah menjadi tanpa parameter 'this' (context)
        viewModel.login(request)
    }

    private fun navigateToDashboard() {
        val intent = if (SessionManager.isStaff(this)) {
            Intent(this, LaboranActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}