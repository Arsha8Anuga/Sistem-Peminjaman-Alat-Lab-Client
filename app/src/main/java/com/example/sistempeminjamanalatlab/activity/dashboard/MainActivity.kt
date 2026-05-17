package com.example.sistempeminjamanalatlab.activity.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sistempeminjamanalatlab.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.sistempeminjamanalatlab.inventaris.AlatListFragment
import com.example.sistempeminjamanalatlab.peminjaman.RiwayatPinjamFragment

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pastikan activity_main.xml kamu sudah berisi FrameLayout/FragmentContainerView & BottomNavigationView
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        // Tampilkan fragment pertama kali (Katalog Alat) saat aplikasi dibuka
        if (savedInstanceState == null) {
            loadFragment(AlatListFragment())
        }

        // Logika perpindahan fragment saat menu bawah diklik
        bottomNavigation.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.menu_katalog -> selectedFragment = AlatListFragment()
                R.id.menu_riwayat -> selectedFragment = RiwayatPinjamFragment()
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment)
                true
            } else {
                false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // R.id.fragment_container wajib ada di activity_main.xml
            .commit()
    }
}