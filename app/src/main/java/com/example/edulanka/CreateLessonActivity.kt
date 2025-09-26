package com.example.edulanka

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CreateLessonActivity : AppCompatActivity() {

    private var videoUri: Uri? = null
    private var thumbUri: Uri? = null

    private lateinit var inputTitle: TextInputEditText
    private lateinit var inputGrade: TextInputEditText
    private lateinit var spinnerSubject: Spinner
    private lateinit var btnPickVideo: Button
    private lateinit var btnPickThumb: Button
    private lateinit var btnUpload: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvVideoName: TextView
    private lateinit var tvThumbName: TextView

    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            videoUri = uri
            tvVideoName.text = uri.lastPathSegment ?: "Video selected"
        }
    }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            thumbUri = uri
            tvThumbName.text = uri.lastPathSegment ?: "Image selected"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_lesson)

        inputTitle = findViewById(R.id.inputTitle)
        inputGrade = findViewById(R.id.inputGrade)
        spinnerSubject = findViewById(R.id.spinnerSubject)
        btnPickVideo = findViewById(R.id.btnPickVideo)
        btnPickThumb = findViewById(R.id.btnPickThumb)
        btnUpload = findViewById(R.id.btnUpload)
        progressBar = findViewById(R.id.progressBar)
        tvVideoName = findViewById(R.id.tvVideoName)
        tvThumbName = findViewById(R.id.tvThumbName)

        // Populate subject list
        val subjects = listOf("Mathematics", "Science", "English", "History")
        spinnerSubject.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, subjects)

        btnPickVideo.setOnClickListener { pickVideo.launch("video/*") }
        btnPickThumb.setOnClickListener { pickImage.launch("image/*") }

        btnUpload.setOnClickListener { uploadLesson() }
    }

    private fun uploadLesson() {
        val title = inputTitle.text?.toString()?.trim().orEmpty()
        val grade = inputGrade.text?.toString()?.trim().orEmpty()
        val subject = spinnerSubject.selectedItem?.toString()?.trim().orEmpty()
        val role = intent.getStringExtra("ROLE") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""

        if (!(role.equals("Lecturer", true) || role.equals("Lecture", true))) {
            Toast.makeText(this, "Only lecturers can upload", Toast.LENGTH_LONG).show()
            return
        }
        if (title.isEmpty() || grade.isEmpty() || subject.isEmpty() || videoUri == null) {
            Toast.makeText(this, "Please enter all fields and select a video", Toast.LENGTH_LONG).show()
            return
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "Not authenticated", Toast.LENGTH_LONG).show()
            return
        }

        progressBar.visibility = android.view.View.VISIBLE
        btnUpload.isEnabled = false

        val storage = FirebaseStorage.getInstance().reference
        val ts = System.currentTimeMillis()
        val videoRef = storage.child("videos/$uid/$ts.mp4")

        // 1) Upload video
        videoRef.putFile(videoUri!!)
            .continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception ?: RuntimeException("Upload failed")
                videoRef.downloadUrl
            }.addOnSuccessListener { videoUrl ->
                // 2) If thumbnail selected, upload then proceed; else proceed
                if (thumbUri != null) {
                    val thumbRef = storage.child("thumbs/$uid/$ts.jpg")
                    thumbRef.putFile(thumbUri!!)
                        .continueWithTask { t ->
                            if (!t.isSuccessful) throw t.exception ?: RuntimeException("Thumb upload failed")
                            thumbRef.downloadUrl
                        }.addOnSuccessListener { thumbUrl ->
                            saveLesson(title, grade, subject, videoUrl.toString(), thumbUrl.toString(), uid, email)
                        }.addOnFailureListener { e ->
                            progressBar.visibility = android.view.View.GONE
                            btnUpload.isEnabled = true
                            Toast.makeText(this, e.localizedMessage ?: "Thumbnail upload failed", Toast.LENGTH_LONG).show()
                        }
                } else {
                    saveLesson(title, grade, subject, videoUrl.toString(), "", uid, email)
                }
            }.addOnFailureListener { e ->
                progressBar.visibility = android.view.View.GONE
                btnUpload.isEnabled = true
                Toast.makeText(this, e.localizedMessage ?: "Video upload failed", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveLesson(
        title: String,
        grade: String,
        subject: String,
        videoUrl: String,
        thumbUrl: String,
        uid: String,
        email: String
    ) {
        val data = hashMapOf(
            "title" to title,
            "grade" to grade,
            "subject" to subject,
            "videoUrl" to videoUrl,
            "thumbUrl" to thumbUrl,
            "createdAt" to FieldValue.serverTimestamp(),
            "createdByUid" to uid,
            "createdBy" to email
        )
        FirebaseFirestore.getInstance().collection("lessons")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Lesson uploaded", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                progressBar.visibility = android.view.View.GONE
                btnUpload.isEnabled = true
                Toast.makeText(this, e.localizedMessage ?: "Failed to save lesson", Toast.LENGTH_LONG).show()
            }
    }
}
