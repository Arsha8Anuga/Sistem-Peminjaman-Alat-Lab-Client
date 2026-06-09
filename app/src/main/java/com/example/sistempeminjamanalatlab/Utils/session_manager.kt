package com.example.sistempeminjamanalatlab.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.sistempeminjamanalatlab.models.entity.User
import com.example.sistempeminjamanalatlab.models.response.LoginData
import com.google.gson.Gson

object SessionManager {

    private const val PREF_NAME = "lab_session"
    private const val KEY_TOKEN = "access_token"
    private const val KEY_ROLE  = "user_role"
    private const val KEY_NAMA  = "user_nama"
    private const val KEY_ID    = "user_id"
    private const val KEY_USER  = "current_user"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveLoginSession(context: Context, data: LoginData) {
        prefs(context).edit().apply {
            putString(KEY_TOKEN, data.accessToken)
            putString(KEY_ROLE, data.role)
            putString(KEY_NAMA, data.nama)
            putLong(KEY_ID, data.userId)
            apply()
        }
    }

    fun saveUserDetail(context: Context, user: User) {
        prefs(context).edit()
            .putString(KEY_USER, Gson().toJson(user))
            .putString(KEY_ROLE, user.role)
            .apply()
    }

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

    fun isLoggedIn(context: Context): Boolean = getToken(context) != null

    fun isStaff(context: Context): Boolean =
        getRole(context) in listOf("laboran", "asisten", "admin")

    fun clearSession(context: Context) {
        prefs(context).edit().clear().apply()
    }
}