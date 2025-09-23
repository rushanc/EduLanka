package com.example.edulanka

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val role = intent.getStringExtra("ROLE") ?: "Unknown"
        val email = intent.getStringExtra("EMAIL") ?: ""

        val title: TextView = findViewById(R.id.homeTitle)
        val subtitle: TextView = findViewById(R.id.homeSubtitle)
        title.text = "Hello, $role"
        subtitle.text = if (email.isNotEmpty()) email else "Welcome to EduLanka"
    }
}
