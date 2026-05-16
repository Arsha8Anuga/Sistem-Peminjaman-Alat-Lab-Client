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
        apiService.getAllAlat("Bearer $token").enqueue(object : Callback<AlatListResponse> {
            override fun onResponse(call: Call<AlatListResponse>, response: Response<AlatListResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body()?.data, response.body()?.message)
                } else {
                    onResult(null, "Gagal mengambil data alat")
                }
            }
            override fun onFailure(call: Call<AlatListResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    fun getAlatDetail(token: String, id: Long, onResult: (Alat?, String?) -> Unit) {
        apiService.getAlatById("Bearer $token", id).enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                onResult(response.body()?.data, response.body()?.message)
            }
            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    fun addAlat(token: String, request: AlatCreateRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.createAlat("Bearer $token", request).enqueue(object : Callback<AlatResponse> {
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
        apiService.getAllKategori("Bearer $token").enqueue(object : Callback<KategoriListResponse> {
            override fun onResponse(call: Call<KategoriListResponse>, response: Response<KategoriListResponse>) {
                onResult(response.body()?.data, response.body()?.message)
            }
            override fun onFailure(call: Call<KategoriListResponse>, t: Throwable) {
                onResult(null, t.message)
            }
        })
    }

    // --- FITUR KATEGORI (Penyesuaian ke KategoriAlat & Bearer) ---

    fun createKategori(token: String, request: KategoriRequest, callback: (Boolean, String?) -> Unit) {
        apiService.createKategori("Bearer $token", request).enqueue(object : Callback<KategoriResponse> {
            override fun onResponse(call: Call<KategoriResponse>, response: Response<KategoriResponse>) {
                if (response.isSuccessful) callback(true, response.body()?.message ?: "Kategori berhasil dibuat")
                else callback(false, "Gagal membuat kategori")
            }
            override fun onFailure(call: Call<KategoriResponse>, t: Throwable) { callback(false, t.message) }
        })
    }

    fun getKategoriById(token: String, id: Long, callback: (KategoriAlat?, String?) -> Unit) {
        apiService.getKategoriById("Bearer $token", id).enqueue(object : Callback<KategoriResponse> {
            override fun onResponse(call: Call<KategoriResponse>, response: Response<KategoriResponse>) {
                if (response.isSuccessful) callback(response.body()?.data, null)
                else callback(null, "Kategori tidak ditemukan")
            }
            override fun onFailure(call: Call<KategoriResponse>, t: Throwable) { callback(null, t.message) }
        })
    }

    fun updateKategori(token: String, id: Long, request: KategoriRequest, callback: (Boolean, String?) -> Unit) {
        apiService.updateKategori("Bearer $token", id, request).enqueue(object : Callback<KategoriResponse> {
            override fun onResponse(call: Call<KategoriResponse>, response: Response<KategoriResponse>) {
                if (response.isSuccessful) callback(true, response.body()?.message ?: "Kategori berhasil diupdate")
                else callback(false, "Gagal mengupdate kategori")
            }
            override fun onFailure(call: Call<KategoriResponse>, t: Throwable) { callback(false, t.message) }
        })
    }

    fun deleteKategori(token: String, id: Long, callback: (Boolean, String?) -> Unit) {
        apiService.deleteKategori("Bearer $token", id).enqueue(object : Callback<BaseResponse> {
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
        apiService.uploadAlatPhoto("Bearer $token", id, imagePart).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    fun updateAlat(token: String, id: Long, request: AlatUpdateRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.updateAlat("Bearer $token", id, request).enqueue(object : Callback<AlatResponse> {
            override fun onResponse(call: Call<AlatResponse>, response: Response<AlatResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<AlatResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }

    fun deleteAlat(token: String, id: Long, onResult: (Boolean, String?) -> Unit) {
        apiService.deleteAlat("Bearer $token", id).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }
}