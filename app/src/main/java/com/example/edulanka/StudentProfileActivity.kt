package com.example.edulanka

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StudentProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        val displayName: TextView = findViewById(R.id.tvDisplayName)
        val roleText: TextView = findViewById(R.id.tvRole)
        val emailValue: TextView = findViewById(R.id.tvEmailValue)
        val gradeValue: TextView = findViewById(R.id.tvGradeValue)
        val languageValue: TextView = findViewById(R.id.tvLanguageValue)

        val email = intent.getStringExtra("EMAIL") ?: "student@example.com"
        val name = intent.getStringExtra("NAME") ?: "Student Name"
        val grade = intent.getStringExtra("GRADE") ?: "Grade 8"
        val language = intent.getStringExtra("LANGUAGE") ?: "English"

        displayName.text = name
        roleText.text = "Student"
        emailValue.text = email
        gradeValue.text = grade
        languageValue.text = language

        // TODO: Add edit profile and change password actions when ready
        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }
    }
}
