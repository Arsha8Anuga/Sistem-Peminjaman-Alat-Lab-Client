package com.example.sistempeminjamanalatlab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistempeminjamanalatlab.models.entity.User
import com.example.sistempeminjamanalatlab.repository.UserRepository
import okhttp3.MultipartBody

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    // ─── DATA STATES ──────────────────────────────────────────

    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> = _userData

    private val _listUsers = MutableLiveData<List<User>?>()
    val listUsers: LiveData<List<User>?> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    // Flag sukses khusus untuk upload foto agar UI bisa memberikan feedback
    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean> = _uploadSuccess

    // ─── FUNGSI UNTUK USER (PROFIL) ──────────────────────────

    /**
     * Mengambil data detail user berdasarkan ID.
     * Sesuai repository: getUserById
     */
    fun getProfile(token: String, userId: Long) {
        _isLoading.value = true
        repository.getUserById(token, userId) { user, msg ->
            _isLoading.value = false
            if (user != null) {
                _userData.value = user
            } else {
                _message.value = msg ?: "Gagal mengambil data profil"
            }
        }
    }

    /**
     * Melakukan upload foto profil.
     * Sesuai repository: uploadProfilePhoto
     */
    fun updatePhoto(token: String, userId: Long, photo: MultipartBody.Part) {
        _isLoading.value = true
        repository.uploadProfilePhoto(token, userId, photo) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            _uploadSuccess.value = success

            // Jika sukses, otomatis ambil data terbaru agar foto di UI terupdate
            if (success) {
                getProfile(token, userId)
            }
        }
    }

    // ─── FUNGSI UNTUK ADMIN ──────────────────────────────────

    /**
     * Mengambil semua daftar user (hanya untuk role Admin).
     * Sesuai repository: getAllUsers
     */
    fun fetchAllUsers(token: String) {
        _isLoading.value = true
        repository.getAllUsers(token) { users, msg ->
            _isLoading.value = false
            _listUsers.value = users
            if (users == null) {
                _message.value = msg ?: "Gagal mengambil daftar user"
            }
        }
    }
}