package com.example.sistempeminjamanalatlab.activity.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.sistempeminjamanalatlab.R
import com.example.sistempeminjamanalatlab.activity.dashboard.LaboranActivity
import com.example.sistempeminjamanalatlab.activity.dashboard.MainActivity
import com.example.sistempeminjamanalatlab.utils.SessionManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkSession()
        }, 2000)
    }

    private fun checkSession() {
        if (SessionManager.isLoggedIn(this)) {
            val intent = if (SessionManager.isStaff(this)) {
                Intent(this, LaboranActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}