package com.example.edulanka

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class LecturerProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecturer_profile)

        val displayName: TextView = findViewById(R.id.tvDisplayName)
        val roleText: TextView = findViewById(R.id.tvRole)
        val emailValue: TextView = findViewById(R.id.tvEmailValue)
        val positionValue: TextView = findViewById(R.id.tvPositionValue)
        val languageValue: TextView = findViewById(R.id.tvLanguageValue)

        val email = intent.getStringExtra("EMAIL") ?: "lecturer@example.com"
        val name = intent.getStringExtra("NAME") ?: "Lecturer Name"
        val position = intent.getStringExtra("POSITION") ?: "Lecturer"
        val language = intent.getStringExtra("LANGUAGE") ?: "English"

        displayName.text = name
        roleText.text = "Lecturer"
        emailValue.text = email
        positionValue.text = position
        languageValue.text = language

        // Upload Video opens CreateLessonActivity
        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tvUploadVideo).setOnClickListener {
            val i = Intent(this, CreateLessonActivity::class.java)
            i.putExtra("ROLE", "Lecturer")
            i.putExtra("EMAIL", email)
            startActivity(i)
        }
    }
}
