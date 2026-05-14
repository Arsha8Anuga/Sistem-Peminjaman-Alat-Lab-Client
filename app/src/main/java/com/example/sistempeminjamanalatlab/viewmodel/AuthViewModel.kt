package com.example.sistempeminjamanalatlab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistempeminjamanalatlab.models.request.LoginRequest
import com.example.sistempeminjamanalatlab.models.request.UserCreateRequest
import com.example.sistempeminjamanalatlab.repository.AuthRepository
import com.example.sistempeminjamanalatlab.utils.SessionManager
import android.content.Context

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // Status Loading (untuk memunculkan ProgressBar di UI)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Pesan Error atau Sukses (untuk Toast/Snackbar)
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    // Status Login Sukses (agar Activity tahu kapan harus pindah screen)
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    // ─── FUNGSI LOGIN ──────────────────────────────────────────

    fun login(context: Context, request: LoginRequest) {
        _isLoading.value = true
        repository.login(request) { data, msg ->
            _isLoading.value = false
            if (data != null) {
                // Simpan token dan data dasar ke SharedPreferences
                SessionManager.saveLoginSession(context, data)
                _loginSuccess.value = true
                _message.value = "Selamat Datang, ${data.nama}!"
            } else {
                _loginSuccess.value = false
                _message.value = msg ?: "Login Gagal, periksa kembali akun Anda"
            }
        }
    }

    // ─── FUNGSI REGISTER ───────────────────────────────────────

    /*fun register(request: UserCreateRequest) {
        _isLoading.value = true
        repository.register(request) { isSuccess, msg ->
            _isLoading.value = false
            _message.value = msg ?: if (isSuccess) "Registrasi berhasil, silakan login" else "Registrasi gagal"
        }
    }*/
}