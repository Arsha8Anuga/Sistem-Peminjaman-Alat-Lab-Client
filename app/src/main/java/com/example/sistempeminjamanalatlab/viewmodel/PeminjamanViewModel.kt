package com.example.sistempeminjamanalatlab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistempeminjamanalatlab.models.entity.Peminjaman
import com.example.sistempeminjamanalatlab.models.request.PeminjamanRequest
import com.example.sistempeminjamanalatlab.models.request.VerifyPengembalianRequest
import com.example.sistempeminjamanalatlab.repository.PeminjamanRepository

class PeminjamanViewModel(private val repository: PeminjamanRepository) : ViewModel() {

    // ─── DATA STATES ──────────────────────────────────────────

    private val _listPeminjaman = MutableLiveData<List<Peminjaman>?>()
    val listPeminjaman: LiveData<List<Peminjaman>?> = _listPeminjaman

    private val _detailPeminjaman = MutableLiveData<Peminjaman?>()
    val detailPeminjaman: LiveData<Peminjaman?> = _detailPeminjaman

    // UI Status
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    // Flag untuk navigasi balik setelah aksi sukses
    private val _actionSuccess = MutableLiveData<Boolean>()
    val actionSuccess: LiveData<Boolean> = _actionSuccess

    // ─── FUNGSI MAHASISWA (USER) ─────────────────────────────

    /** Mahasiswa mengajukan pinjaman alat baru */
    fun submitPeminjaman(token: String, request: PeminjamanRequest) {
        _isLoading.value = true
        repository.submitPeminjaman(token, request) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            _actionSuccess.value = success
        }
    }

    /** Mengambil riwayat peminjaman (baik untuk Mhs maupun List All untuk Laboran) */
    fun fetchHistory(token: String) {
        _isLoading.value = true
        repository.getMyLoanHistory(token) { data, msg ->
            _isLoading.value = false
            _listPeminjaman.value = data
            if (data == null) _message.value = msg ?: "Gagal memuat riwayat"
        }
    }

    /** Melihat detail satu transaksi (termasuk list item alatnya) */
    fun fetchDetail(token: String, loanId: Long) {
        _isLoading.value = true
        repository.getLoanDetail(token, loanId) { data, msg ->
            _isLoading.value = false
            _detailPeminjaman.value = data
            if (data == null) _message.value = msg ?: "Detail tidak ditemukan"
        }
    }

    // ─── FUNGSI LABORAN (ADMIN/STAFF) ────────────────────────

    /** Menyetujui pengajuan (Status: Pending -> Approved) */
    fun approve(token: String, loanId: Long) {
        _isLoading.value = true
        repository.approveLoan(token, loanId) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            if (success) fetchDetail(token, loanId) // Refresh detail status
        }
    }

    /** Menolak pengajuan (Status: Pending -> Rejected) */
    fun reject(token: String, loanId: Long) {
        _isLoading.value = true
        repository.rejectLoan(token, loanId) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            if (success) _actionSuccess.value = true
        }
    }

    /** Update saat alat fisik diambil oleh mhs (Status: Approved -> On Loan) */
    fun markAsTaken(token: String, loanId: Long) {
        _isLoading.value = true
        repository.ambilAlat(token, loanId) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            if (success) fetchDetail(token, loanId)
        }
    }

    /** Laboran memverifikasi pengembalian alat (Status: On Loan -> Returned/Done) */
    fun verifyReturn(token: String, loanId: Long, request: VerifyPengembalianRequest) {
        _isLoading.value = true
        repository.verifyPengembalian(token, loanId, request) { success, msg ->
            _isLoading.value = false
            _message.value = msg
            _actionSuccess.value = success
        }
    }
}