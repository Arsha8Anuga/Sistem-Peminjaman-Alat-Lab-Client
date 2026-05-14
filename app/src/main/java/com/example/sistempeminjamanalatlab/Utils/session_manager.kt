package com.example.sistempeminjamanalatlab.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.sistempeminjamanalatlab.models.entity.User
import com.example.sistempeminjamanalatlab.models.response.LoginData
import com.google.gson.Gson

/**
 * SessionManager
 * Menyimpan data login user ke SharedPreferences.
 *
 * Penggunaan:
 *   SessionManager.saveSession(context, loginData, token)
 *   SessionManager.getToken(context)
 *   SessionManager.getUser(context)
 *   SessionManager.isLoggedIn(context)
 *   SessionManager.clearSession(context)
 */
object SessionManager {

    private const val PREF_NAME = "lab_session"
    private const val KEY_TOKEN = "access_token"
    private const val KEY_ROLE  = "user_role"
    private const val KEY_NAMA  = "user_nama"
    private const val KEY_ID    = "user_id"
    private const val KEY_USER  = "current_user"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // ─── SIMPAN DATA DARI LOGIN ──────────────────────────

    /** * Dipanggil setelah login sukses menggunakan data dari LoginData
     */
    fun saveLoginSession(context: Context, data: LoginData) {
        prefs(context).edit().apply {
            putString(KEY_TOKEN, data.accessToken)
            putString(KEY_ROLE, data.role)
            putString(KEY_NAMA, data.nama)
            putLong(KEY_ID, data.userId)
            apply()
        }
    }

    // ─── SIMPAN / UPDATE OBJEK USER LENGKAP ──────────────

    /**
     * Dipanggil saat ambil detail profil (User Entity)
     */
    fun saveUserDetail(context: Context, user: User) {
        prefs(context).edit()
            .putString(KEY_USER, Gson().toJson(user))
            .putString(KEY_ROLE, user.role) // Sinkronkan ulang role jika berubah
            .apply()
    }

    // ─── AMBIL DATA ───────────────────────────────────────

    fun getToken(context: Context): String? =
        prefs(context).getString(KEY_TOKEN, null)

    fun getBearerToken(context: Context): String? {
        val token = getToken(context)
        return if (token != null) "Bearer $token" else null
    }

    fun getRole(context: Context): String? =
        prefs(context).getString(KEY_ROLE, null)

    fun getUserId(context: Context): Long =
        prefs(context).getLong(KEY_ID, -1L)

    fun getUser(context: Context): User? {
        val json = prefs(context).getString(KEY_USER, null) ?: return null
        return Gson().fromJson(json, User::class.java)
    }

    // ─── LOGIKA STATUS ────────────────────────────────────

    fun isLoggedIn(context: Context): Boolean = getToken(context) != null

    // Helper Role (Tetap seperti kodinganmu karena sudah bagus)
    fun isStaff(context: Context): Boolean =
        getRole(context) in listOf("laboran", "asisten", "admin")

    // ─── HAPUS ───────────────────────────────────────────

    fun clearSession(context: Context) {
        prefs(context).edit().clear().apply()
    }
}