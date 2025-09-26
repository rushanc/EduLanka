package com.example.edulanka

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class CreateAnnouncementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_announcement)

        val inputTitle: EditText = findViewById(R.id.inputTitle)
        val inputMessage: EditText = findViewById(R.id.inputMessage)
        val btnPost: Button = findViewById(R.id.btnPost)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnCancel.setOnClickListener { finish() }

        btnPost.setOnClickListener {
            val title = inputTitle.text.toString().trim()
            val message = inputMessage.text.toString().trim()
            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please enter title and message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = intent.getStringExtra("EMAIL") ?: ""
            val role = intent.getStringExtra("ROLE") ?: ""
            if (!(role.equals("Lecturer", true) || role.equals("Lecture", true))) {
                Toast.makeText(this, "Only lecturers can post announcements", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val data = hashMapOf(
                "title" to title,
                "message" to message,
                "publishedAt" to FieldValue.serverTimestamp(),
                "createdBy" to email,
                "createdByUid" to uid,
                "role" to role
            )

            FirebaseFirestore.getInstance().collection("announcements")
                .add(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Announcement posted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.localizedMessage ?: "Failed to post", Toast.LENGTH_LONG).show()
                }
        }
    }
}
