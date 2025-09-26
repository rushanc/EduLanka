package com.example.edulanka

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edulanka.home.AnnouncementAdapter
import com.example.edulanka.model.Announcement
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import androidx.appcompat.app.AlertDialog

class AnnouncementsActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private val items = mutableListOf<Announcement>()
    private lateinit var adapter: AnnouncementAdapter
    private var isLecturer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcements)

        // Back button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        rv = findViewById(R.id.rvAnnouncements)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = AnnouncementAdapter(
            items,
            onClick = { /* TODO: open detail */ },
            onLongClick = { ann ->
                if (isLecturer && ann.id != null) confirmDelete(ann.id)
            }
        )
        rv.adapter = adapter

        // Show FAB only for lecturers
        val role = intent.getStringExtra("ROLE") ?: ""
        isLecturer = role.equals("Lecturer", true) || role.equals("Lecture", true)
        val fab: FloatingActionButton = findViewById(R.id.fabAdd)
        if (isLecturer) {
            fab.visibility = View.VISIBLE
            fab.setOnClickListener {
                val email = intent.getStringExtra("EMAIL")
                val i = Intent(this, CreateAnnouncementActivity::class.java)
                i.putExtra("ROLE", role)
                if (email != null) i.putExtra("EMAIL", email)
                startActivity(i)
            }
        } else {
            fab.visibility = View.GONE
        }

        subscribeAnnouncements()
    }

    private fun subscribeAnnouncements() {
        FirebaseFirestore.getInstance()
            .collection("announcements")
            .orderBy("publishedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { qs, _ ->
                if (qs == null) return@addSnapshotListener
                items.clear()
                for (doc in qs.documents) {
                    val title = doc.getString("title") ?: ""
                    val message = doc.getString("message") ?: ""
                    val ts = doc.getTimestamp("publishedAt") ?: Timestamp(Date())
                    items.add(
                        Announcement(
                            id = doc.id,
                            title = title,
                            message = message,
                            timeLabel = toTimeLabel(ts.toDate())
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun confirmDelete(docId: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete announcement?")
            .setMessage("This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                FirebaseFirestore.getInstance()
                    .collection("announcements")
                    .document(docId)
                    .delete()
                    .addOnSuccessListener { /* List updates via snapshot listener */ }
                    .addOnFailureListener { /* show error if needed */ }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun toTimeLabel(date: Date): String {
        val cal = Calendar.getInstance()
        val today = cal.clone() as Calendar
        stripTime(today)

        val target = Calendar.getInstance().apply { time = date }
        val targetDay = target.clone() as Calendar
        stripTime(targetDay)

        val diffDays = ((today.timeInMillis - targetDay.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()
        return when (diffDays) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
        }
    }

    private fun stripTime(c: Calendar) {
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
    }
}
