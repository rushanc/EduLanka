package com.example.edulanka

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val role = intent.getStringExtra("ROLE") ?: "Unknown"
        val email = intent.getStringExtra("EMAIL") ?: ""

        val title: TextView = findViewById(R.id.homeTitle)
        val subtitle: TextView = findViewById(R.id.homeSubtitle)
        val profileBtn: Button = findViewById(R.id.btnGoProfile)
        title.text = "Hello, $role"
        subtitle.text = if (email.isNotEmpty()) email else "Welcome to EduLanka"

        profileBtn.setOnClickListener {
            val intent = if (role.equals("Lecturer", ignoreCase = true) || role.equals("Lecture", ignoreCase = true)) {
                Intent(this, LecturerProfileActivity::class.java)
            } else {
                Intent(this, StudentProfileActivity::class.java)
            }
            intent.putExtra("EMAIL", email)
            intent.putExtra("NAME", "User Name")
            startActivity(intent)
        }
    }
}
