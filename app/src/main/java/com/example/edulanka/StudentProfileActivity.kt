package com.example.edulanka

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        // Logout
        findViewById<TextView>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i = Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(i)
            finish()
        }

        // Load registered name/email from Firestore if available
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    val regName = doc.getString("name")
                    val regEmail = doc.getString("email")
                    if (!regName.isNullOrEmpty()) displayName.text = regName
                    if (!regEmail.isNullOrEmpty()) emailValue.text = regEmail
                }
        }
    }
}
