package com.example.sistempeminjamanalatlab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sistempeminjamanalatlab.repository.*

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository? = null,
    private val alatRepository: AlatRepository? = null,
    private val peminjamanRepository: PeminjamanRepository? = null,
    private val pengembalianRepository: PengembalianRepository? = null,
    private val userRepository: UserRepository? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                requireNotNull(authRepository) { "AuthRepository tidak boleh null untuk AuthViewModel" }
                AuthViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(AlatViewModel::class.java) -> {
                requireNotNull(alatRepository) { "AlatRepository tidak boleh null untuk AlatViewModel" }
                AlatViewModel(alatRepository) as T
            }
            modelClass.isAssignableFrom(PeminjamanViewModel::class.java) -> {
                requireNotNull(peminjamanRepository) { "PeminjamanRepository tidak boleh null untuk PeminjamanViewModel" }
                // Sesuaikan jika kamu memilih Opsi B di tahap sebelumnya (menyuntikkan pengembalianRepository juga)
                PeminjamanViewModel(peminjamanRepository) as T
            }
            modelClass.isAssignableFrom(PengembalianViewModel::class.java) -> {
                requireNotNull(pengembalianRepository) { "PengembalianRepository tidak boleh null untuk PengembalianViewModel" }
                PengembalianViewModel(pengembalianRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                requireNotNull(userRepository) { "UserRepository tidak boleh null untuk ProfileViewModel" }
                ProfileViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        // Fungsi pembantu (Helper) untuk membuat instance Factory secara spesifik di tiap Activity
        fun getInstance(authRepo: AuthRepository) = ViewModelFactory(authRepository = authRepo)
        fun getInstance(alatRepo: AlatRepository) = ViewModelFactory(alatRepository = alatRepo)
        fun getInstance(pemRepo: PeminjamanRepository) = ViewModelFactory(peminjamanRepository = pemRepo)
        fun getInstance(pengRepo: PengembalianRepository) = ViewModelFactory(pengembalianRepository = pengRepo)
        fun getInstance(userRepo: UserRepository) = ViewModelFactory(userRepository = userRepo)
    }
}