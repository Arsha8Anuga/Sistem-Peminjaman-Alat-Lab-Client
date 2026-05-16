package com.example.sistempeminjamanalatlab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistempeminjamanalatlab.repository.UserRepository
import okhttp3.MultipartBody

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    // ─── DATA STATES (Hanya menyisakan yang diperlukan untuk Upload) ───

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean> = _uploadSuccess

    // ─── FUNGSI UTAMA ─────────────────────────────────────────

    /**
     * Melakukan upload foto profil.
     * Sesuai repository: uploadProfilePhoto yang terhubung ke @POST("upload/user/{user_id}")
     */
    fun updatePhoto(token: String, userId: Long, photo: MultipartBody.Part) {
        _isLoading.value = true
        repository.uploadProfilePhoto(token, userId, photo) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            _uploadSuccess.value = success
            // Catatan: getProfile(token, userId) dihapus karena data profil utama
            // di-manage oleh SessionManager lokal di Activity.
        }
    }

    /** Reset state upload agar feedback di UI tidak muncul berulang */
    fun resetUploadState() {
        _uploadSuccess.value = false
    }
}