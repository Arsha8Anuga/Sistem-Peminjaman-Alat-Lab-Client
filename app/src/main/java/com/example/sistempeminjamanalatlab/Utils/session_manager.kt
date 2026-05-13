package com.example.sistempeminjamanalatlab.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.sistempeminjamanalatlab.models.entity.User
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

    private const val PREF_NAME      = "lab_session"
    private const val KEY_TOKEN      = "access_token"
    private const val KEY_USER       = "current_user"
    private const val KEY_ROLE       = "user_role"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // ─── SIMPAN ──────────────────────────────────────────

    fun saveSession(context: Context, user: User, token: String) {
        prefs(context).edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_ROLE, user.role)
            .putString(KEY_USER, Gson().toJson(user))
            .apply()
    }

    // ─── AMBIL ───────────────────────────────────────────

    fun getToken(context: Context): String? =
        prefs(context).getString(KEY_TOKEN, null)

    /** Kembalikan token dalam format "Bearer <token>" siap pakai di header */
    fun getBearerToken(context: Context): String =
        "Bearer ${getToken(context)}"

    fun getRole(context: Context): String? =
        prefs(context).getString(KEY_ROLE, null)

    fun getUser(context: Context): User? {
        val json = prefs(context).getString(KEY_USER, null) ?: return null
        return Gson().fromJson(json, User::class.java)
    }

    // ─── CEK STATUS ──────────────────────────────────────

    fun isLoggedIn(context: Context): Boolean =
        getToken(context) != null

    fun isMahasiswa(context: Context): Boolean =
        getRole(context) == "mahasiswa"

    fun isLaboran(context: Context): Boolean =
        getRole(context) == "laboran"

    fun isAsisten(context: Context): Boolean =
        getRole(context) == "asisten"

    fun isAdmin(context: Context): Boolean =
        getRole(context) == "admin"

    fun isStaff(context: Context): Boolean =
        getRole(context) in listOf("laboran", "asisten", "admin")

    // ─── HAPUS ───────────────────────────────────────────

    fun clearSession(context: Context) {
        prefs(context).edit().clear().apply()
    }
}