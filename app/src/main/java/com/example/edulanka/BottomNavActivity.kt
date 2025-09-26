package com.example.edulanka

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val browseFragment = BrowseFragment()
    private val progressFragment = ProgressFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        // Set initial fragment
        loadFragment(homeFragment)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> loadFragment(homeFragment)
                R.id.menu_browse -> loadFragment(browseFragment)
                R.id.menu_progress -> loadFragment(progressFragment)
                R.id.menu_profile -> {
                    val role = intent.getStringExtra("ROLE") ?: "Student"
                    val email = intent.getStringExtra("EMAIL") ?: ""
                    val profileIntent = if (role.equals("Lecturer", true) || role.equals("Lecture", true))
                        Intent(this, LecturerProfileActivity::class.java)
                    else
                        Intent(this, StudentProfileActivity::class.java)
                    profileIntent.putExtra("EMAIL", email)
                    profileIntent.putExtra("NAME", "User Name")
                    startActivity(profileIntent)
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
