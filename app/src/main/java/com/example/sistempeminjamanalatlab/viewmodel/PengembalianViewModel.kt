package com.example.sistempeminjamanalatlab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistempeminjamanalatlab.models.entity.Pengembalian
import com.example.sistempeminjamanalatlab.models.request.PengembalianRequest
import com.example.sistempeminjamanalatlab.models.request.VerifyPengembalianRequest
import com.example.sistempeminjamanalatlab.repository.PengembalianRepository

class PengembalianViewModel(private val repository: PengembalianRepository) : ViewModel() {

    // ─── DATA STATES ──────────────────────────────────────────

    private val _detailPengembalian = MutableLiveData<Pengembalian?>()
    val detailPengembalian: LiveData<Pengembalian?> = _detailPengembalian

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _actionSuccess = MutableLiveData<Boolean>()
    val actionSuccess: LiveData<Boolean> = _actionSuccess

    // ─── FUNGSI UTAMA ─────────────────────────────────────────

    /**
     * 1. Ambil Detail Pengembalian
     * Dipakai untuk mengecek apakah denda sudah muncul atau status sudah diverifikasi.
     */
    fun fetchDetail(token: String, peminjamanId: Long) {
        _isLoading.value = true
        repository.getDetailPengembalian(token, peminjamanId) { data, msg ->
            _isLoading.value = false
            _detailPengembalian.value = data
            if (data == null && msg != null) _message.value = msg
        }
    }

    /**
     * 2. Submit Form Pengembalian (Mahasiswa)
     * Dipanggil saat mahasiswa menekan tombol "Kembalikan Alat" di aplikasi.
     */
    fun submitReturn(token: String, request: PengembalianRequest) {
        _isLoading.value = true
        repository.submitPengembalian(token, request) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            _actionSuccess.value = success
        }
    }

    /**
     * 3. Verifikasi Pengembalian (Laboran)
     * Dipanggil laboran saat memeriksa fisik alat dan menentukan denda.
     */
    fun verifyReturn(token: String, peminjamanId: Long, request: VerifyPengembalianRequest) {
        _isLoading.value = true
        repository.verifyPengembalian(token, peminjamanId, request) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            _actionSuccess.value = success
        }
    }
}