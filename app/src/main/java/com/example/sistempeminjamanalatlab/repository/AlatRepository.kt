package com.example.sistempeminjamanalatlab.repository

import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.entity.Alat
import com.example.sistempeminjamanalatlab.models.entity.KategoriAlat
import com.example.sistempeminjamanalatlab.models.request.AlatCreateRequest
import com.example.sistempeminjamanalatlab.models.request.AlatUpdateRequest
import com.example.sistempeminjamanalatlab.models.request.KategoriRequest
import com.example.sistempeminjamanalatlab.models.response.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlatRepository(private val apiService: APIService) {

    // --- FITUR ALAT ---

    fun getAllAlat(token: String, onResult: (List<Alat>?, String?) -> Unit) {
        apiService.getAllAlat(token).enqueue(object : Callback<List<Alat>> { // 🟢 Ganti jadi List<Alat>
            override fun onResponse(call: Call<List<Alat>>, response: Response<List<Alat>>) {
                if (response.isSuccessful) {
                    // 🟢 Langsung ambil response.body() karena datanya polosan tanpa bungkus .data
                    onResult(response.body(), "Sukses memuat alat")
                } else {
                    onResult(null, "Gagal memuat data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Alat>>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    fun getAlatDetail(token: String, id: Long, onResult: (Alat?, String?) -> Unit) {
        apiService.getAlatById(token, id).enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                onResult(response.body()?.data, response.body()?.message)
            }
            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    fun addAlat(token: String, request: AlatCreateRequest, onResult: (Boolean, String?) -> Unit) {
        val kondisiBackend = when (request.kondisiFisik.trim()) {
            "Baik" -> "baik"
            "Rusak Ringan" -> "rusak_ringan"
            "Rusak Berat" -> "rusak_berat"
            else -> request.kondisiFisik.lowercase() // Jaga-jaga jika teksnya sudah huruf kecil
        }

        apiService.createAlat(
            token = token,
            namaAlat = request.namaAlat,
            kodeAlat = request.kodeAlat,
            kategoriId = request.kategoriId,
            stokTotal = request.stokTotal,
            stokTersedia = request.stokTotal, // Kita samakan dulu stok tersedia dengan stok total saat awal dibuat
            kondisiFisik = kondisiBackend,
            statusKetersediaan = "tersedia", // Default status awal alat baru
            lokasiPenyimpanan = "Gudang Lab"  // Menjawab field yang diminta wajib oleh FastAPI kamu
        ).enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    // --- FITUR KATEGORI (Baru) ---

    fun getAllKategori(token: String, onResult: (List<KategoriAlat>?, String?) -> Unit) {
        apiService.getAllKategori(token).enqueue(object : Callback<List<KategoriAlat>> { // 🟢 Ini tipe targetnya

            override fun onResponse(
                call: Call<List<KategoriAlat>>,
                response: Response<List<KategoriAlat>>
            ) { // 🟢 Pastikan parameternya murni List<KategoriAlat>
                if (response.isSuccessful) {
                    onResult(response.body(), "Berhasil memuat kategori")
                } else {
                    onResult(null, "Gagal mengambil data kategori")
                }
            }

            override fun onFailure(call: Call<List<KategoriAlat>>, t: Throwable) { // 🟢 Ini juga disamakan
                onResult(null, t.message)
            }
        })
    }

    // --- FITUR KATEGORI (Penyesuaian ke KategoriAlat & Bearer) ---

    fun createKategori(token: String, request: KategoriRequest, callback: (Boolean, String?) -> Unit) {
        apiService.createKategori(token, request).enqueue(object : Callback<KategoriResponse> {
            override fun onResponse(call: Call<KategoriResponse>, response: Response<KategoriResponse>) {
                if (response.isSuccessful) callback(true, response.body()?.message ?: "Kategori berhasil dibuat")
                else callback(false, "Gagal membuat kategori")
            }
            override fun onFailure(call: Call<KategoriResponse>, t: Throwable) { callback(false, t.message) }
        })
    }

    fun getKategoriById(token: String, id: Long, callback: (KategoriAlat?, String?) -> Unit) {
        apiService.getKategoriById(token, id).enqueue(object : Callback<KategoriResponse> {
            override fun onResponse(call: Call<KategoriResponse>, response: Response<KategoriResponse>) {
                if (response.isSuccessful) callback(response.body()?.data, null)
                else callback(null, "Kategori tidak ditemukan")
            }
            override fun onFailure(call: Call<KategoriResponse>, t: Throwable) { callback(null, t.message) }
        })
    }

    fun updateKategori(token: String, id: Long, request: KategoriRequest, callback: (Boolean, String?) -> Unit) {
        apiService.updateKategori(token, id, request).enqueue(object : Callback<KategoriResponse> {
            override fun onResponse(call: Call<KategoriResponse>, response: Response<KategoriResponse>) {
                if (response.isSuccessful) callback(true, response.body()?.message ?: "Kategori berhasil diupdate")
                else callback(false, "Gagal mengupdate kategori")
            }
            override fun onFailure(call: Call<KategoriResponse>, t: Throwable) { callback(false, t.message) }
        })
    }

    fun deleteKategori(token: String, id: Long, callback: (Boolean, String?) -> Unit) {
        apiService.deleteKategori(token, id).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) callback(true, response.body()?.message ?: "Kategori berhasil dihapus")
                else callback(false, "Gagal menghapus kategori")
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) { callback(false, t.message) }
        })
    }

    // --- FITUR UPLOAD FOTO (Baru) ---
    // Dipanggil setelah addAlat sukses mendapatkan ID
    fun uploadFotoAlat(token: String, id: Long, imagePart: MultipartBody.Part, onResult: (Boolean, String?) -> Unit) {
        apiService.uploadAlatPhoto(token, id, imagePart).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    fun updateAlat(token: String, id: Long, request: AlatUpdateRequest, onResult: (Boolean, String?) -> Unit) {

        // 1. Amankan konversi kondisi fisik dan berikan fallback "" jika null
        val kondisiFisikAman = request.kondisiFisik?.trim() ?: "baik"
        val kondisiBackend = when (kondisiFisikAman) {
            "Baik" -> "baik"
            "Rusak Ringan" -> "rusak_ringan"
            "Rusak Berat" -> "rusak_berat"
            else -> kondisiFisikAman.lowercase()
        }

        // 2. Kirim ke API dengan memberikan nilai default menggunakan operator ?:
        apiService.updateAlat(
            token = token,
            id = id,
            namaAlat = request.namaAlat ?: "Tanpa Nama",                 // 🟢 Jika null, ganti teks ini
            kodeAlat = request.kodeAlat ?: "",                          // 🟢 Jika null, ganti string kosong
            kategoriId = request.kategoriId ?: 0L,                       // 🟢 Jika Long? null, ganti jadi 0L
            stokTotal = request.stokTotal ?: 0,                          // 🟢 Jika Int? null, ganti jadi 0
            stokTersedia = request.stokTersedia ?: 0,                    // 🟢 Jika Int? null, ganti jadi 0
            kondisiFisik = kondisiBackend,                               // Sudah aman di atas
            statusKetersediaan = request.statusKetersediaan ?: "tersedia", // 🟢 Jika null, ganti "tersedia"
            lokasiPenyimpanan = request.lokasiPenyimpanan ?: "Gudang Lab"
        ).enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                if (response.isSuccessful) {
                    onResult(true, response.body()?.message ?: "Alat berhasil diperbarui")
                } else {
                    onResult(false, "Gagal memperbarui alat (Code: ${response.code()})")
                }
            }

            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    fun deleteAlat(token: String, id: Long, onResult: (Boolean, String?) -> Unit) {
        apiService.deleteAlat(token, id).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }
}