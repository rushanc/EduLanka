package com.example.edulanka

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LessonDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("TITLE") ?: "Lesson"
        val videoUrl = intent.getStringExtra("VIDEO_URL")
        if (videoUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid video URL", Toast.LENGTH_LONG).show()
            finish()
            return
        }

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
}
