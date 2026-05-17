package com.example.sistempeminjamanalatlab.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.sistempeminjamanalatlab.models.response.*
import com.example.sistempeminjamanalatlab.api.APIService
import com.example.sistempeminjamanalatlab.models.request.LoginRequest

class AuthRepository(private val apiService: APIService) {
    fun login(request: LoginRequest, onResult: (LoginData?, String?) -> Unit) {
        // 🟢 Panggil fungsi login yang mengarah ke ActualLoginResponse (tanpa wrapper)
        apiService.loginUser(request).enqueue(object : Callback<ActualLoginResponse> {
            override fun onResponse(
                call: Call<ActualLoginResponse>,
                response: Response<ActualLoginResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val token = body.accessToken

                        // 🟢 1. BONGKAR JWT TOKEN UNTUK AMBIL USER_ID & ROLE
                        val (userId, role) = decodeJWT(token)

                        // 🟢 2. RAKIT MENJADI OBJEK LOGINDATA YANG DIHARAPKAN VIEWMODEL
                        val loginData = LoginData(
                            userId = userId,
                            accessToken = token,
                            tokenType = body.tokenType,
                            role = role,
                            nama = "User Lab", // Default sementara karena Python tidak return nama saat login
                            nimNip = ""
                        )

                        // 🟢 3. Kembalikan data sukses ke ViewModel
                        onResult(loginData, "Login Berhasil")
                    } else {
                        onResult(null, "Respons kosong dari server")
                    }
                } else {
                    onResult(null, "Email atau Password salah!")
                }
            }

            override fun onFailure(call: Call<ActualLoginResponse>, t: Throwable) {
                onResult(null, "Tidak ada koneksi internet: ${t.message}")
            }
        })
    }

    // 🛠️ FUNGSI HELPER UNTUK MEMBONGKAR JWT (Taruh di dalam kelas AuthRepository)
    private fun decodeJWT(jwtToken: String): Pair<Long, String> {
        return try {
            val parts = jwtToken.split(".")
            if (parts.size < 2) return Pair(-1L, "mahasiswa")

            // Mengambil bagian Payload (tengah) JWT dan men-decode dari Base64
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT))
            val jsonObject = org.json.JSONObject(payload)

            val userId = jsonObject.optString("sub", "-1").toLongOrNull() ?: -1L
            val role = jsonObject.optString("role", "mahasiswa")

            Pair(userId, role)
        } catch (e: Exception) {
            Pair(-1L, "mahasiswa") // Fallback jika decode gagal
        }
    }

    /*fun register(request: UserCreateRequest, onResult: (Boolean, String?) -> Unit) {
        apiService.registerUser(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                onResult(response.isSuccessful, response.body()?.message)
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }*/
}