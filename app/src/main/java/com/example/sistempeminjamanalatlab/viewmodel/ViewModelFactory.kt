package com.example.sistempeminjamanalatlab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sistempeminjamanalatlab.repository.AuthRepository
import com.example.sistempeminjamanalatlab.repository.AlatRepository

class ViewModelFactory(private val repository: Any) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // Jika yang diminta adalah AuthViewModel
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository as AuthRepository) as T
            }
            // Jika yang diminta adalah AlatViewModel
            modelClass.isAssignableFrom(AlatViewModel::class.java) -> {
                AlatViewModel(repository as AlatRepository) as T
            }
            // Tambahkan ViewModel lainnya di sini...
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}