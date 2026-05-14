package com.example.sistempeminjamanalatlab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistempeminjamanalatlab.models.entity.Alat
import com.example.sistempeminjamanalatlab.models.entity.KategoriAlat
import com.example.sistempeminjamanalatlab.models.request.AlatCreateRequest
import com.example.sistempeminjamanalatlab.models.request.AlatUpdateRequest
import com.example.sistempeminjamanalatlab.repository.AlatRepository
import okhttp3.MultipartBody

class AlatViewModel(private val repository: AlatRepository) : ViewModel() {

    // ─── STATE / DATA ──────────────────────────────────────────

    private val _listAlat = MutableLiveData<List<Alat>?>()
    val listAlat: LiveData<List<Alat>?> = _listAlat

    private val _listKategori = MutableLiveData<List<KategoriAlat>?>()
    val listKategori: LiveData<List<KategoriAlat>?> = _listKategori

    private val _detailAlat = MutableLiveData<Alat?>()
    val detailAlat: LiveData<Alat?> = _detailAlat

    // Status UI
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    // Flag sukses untuk navigasi (setelah tambah/update/hapus)
    private val _actionSuccess = MutableLiveData<Boolean>()
    val actionSuccess: LiveData<Boolean> = _actionSuccess

    // ─── FUNGSI AMBIL DATA (READ) ─────────────────────────────

    fun fetchAllAlat(token: String) {
        _isLoading.value = true
        repository.getAllAlat(token) { data, msg ->
            _isLoading.value = false
            _listAlat.value = data
            if (data == null) _message.value = msg ?: "Gagal memuat alat"
        }
    }

    fun fetchAlatDetail(token: String, id: Long) {
        _isLoading.value = true
        repository.getAlatDetail(token, id) { data, msg ->
            _isLoading.value = false
            _detailAlat.value = data
            if (data == null) _message.value = msg ?: "Detail tidak ditemukan"
        }
    }

    fun fetchKategori(token: String) {
        repository.getAllKategori(token) { data, _ ->
            _listKategori.value = data
        }
    }

    // ─── FUNGSI MANAJEMEN (CRUD) ──────────────────────────────

    fun insertAlat(token: String, request: AlatCreateRequest) {
        _isLoading.value = true
        repository.addAlat(token, request) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            _actionSuccess.value = success
        }
    }

    fun updateAlat(token: String, id: Long, request: AlatUpdateRequest) {
        _isLoading.value = true
        repository.updateAlat(token, id, request) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            _actionSuccess.value = success
        }
    }

    fun deleteAlat(token: String, id: Long) {
        _isLoading.value = true
        repository.deleteAlat(token, id) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            if (success) {
                _actionSuccess.value = true
                // Refresh list setelah hapus
                fetchAllAlat(token)
            }
        }
    }

    // ─── UPLOAD FOTO ──────────────────────────────────────────

    fun uploadFoto(token: String, id: Long, imagePart: MultipartBody.Part) {
        _isLoading.value = true
        repository.uploadFotoAlat(token, id, imagePart) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            if (success) fetchAlatDetail(token, id) // Refresh detail agar foto muncul
        }
    }
}