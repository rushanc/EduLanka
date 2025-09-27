package com.example.edulanka

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class LessonDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("TITLE") ?: "Lesson"
        val videoUrl = intent.getStringExtra("VIDEO_URL")
        val lessonId = intent.getStringExtra("LESSON_ID")
        val subject = intent.getStringExtra("SUBJECT") ?: ""
        if (videoUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid video URL", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Record watch event (best-effort, non-blocking)
        recordWatchEvent(lessonId, subject)

        // Open with an external player for simplicity; we can embed ExoPlayer later
        val i = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(videoUrl), "video/*")
            putExtra("title", title)
        }
        // Validate there is an app to handle the intent
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i)
        } else {
            // Fallback to default ACTION_VIEW without mime
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)))
        }
        finish()
    }

    private fun recordWatchEvent(lessonId: String?, subject: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        // 1) Mark lesson as watched under userProgress/{uid}/watchedLessons/{lessonId}
        if (!lessonId.isNullOrEmpty()) {
            val watchedRef = db.collection("userProgress").document(uid)
                .collection("watchedLessons").document(lessonId)
            watchedRef.set(
                mapOf(
                    "subject" to subject,
                    "watchedAt" to FieldValue.serverTimestamp()
                )
            )
        }

        // 2) Increment subject counter in userProgress/{uid}
        if (subject.isNotEmpty()) {
            val countersRef = db.collection("userProgress").document(uid)
            val field = when (subject.lowercase()) {
                "mathematics", "math", "maths" -> "mathCount"
                "science" -> "scienceCount"
                "english" -> "englishCount"
                "history" -> "historyCount"
                else -> "otherCount"
            }
            countersRef.set(mapOf(field to FieldValue.increment(1)), /* merge */ com.google.firebase.firestore.SetOptions.merge())
        }
    }
}
