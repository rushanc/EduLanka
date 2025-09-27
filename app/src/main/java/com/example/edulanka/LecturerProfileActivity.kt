package com.example.edulanka

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
