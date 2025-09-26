package com.example.edulanka

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edulanka.home.AnnouncementAdapter
import com.example.edulanka.home.FeaturedAdapter
import com.example.edulanka.model.Announcement
import com.example.edulanka.model.FeaturedLesson

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Featured lessons
        val featured: RecyclerView = view.findViewById(R.id.rvFeatured)
        featured.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val featuredItems = listOf(
            FeaturedLesson("Introduction to Algebra", R.mipmap.ic_launcher),
            FeaturedLesson("The Solar System", R.mipmap.ic_launcher),
            FeaturedLesson("World History: Ancient Civilizations", R.mipmap.ic_launcher)
        )
        featured.adapter = FeaturedAdapter(featuredItems) {
            // TODO: open video detail when available
        }

        // Quick access
        view.findViewById<View>(R.id.btnQuiz).setOnClickListener {
            startActivity(Intent(requireContext(), QuizActivity::class.java))
        }
        view.findViewById<View>(R.id.btnDownloads).setOnClickListener {
            startActivity(Intent(requireContext(), DownloadsActivity::class.java))
        }
        view.findViewById<View>(R.id.btnAnnouncements).setOnClickListener {
            val role = activity?.intent?.getStringExtra("ROLE")
            val email = activity?.intent?.getStringExtra("EMAIL")
            val i = Intent(requireContext(), AnnouncementsActivity::class.java)
            if (role != null) i.putExtra("ROLE", role)
            if (email != null) i.putExtra("EMAIL", email)
            startActivity(i)
        }

        // Announcements
        val anns: RecyclerView = view.findViewById(R.id.rvAnnouncements)
        anns.layoutManager = LinearLayoutManager(requireContext())
        val annItems = listOf(
            Announcement(
                title = "New Science Course Available",
                message = "Explore our new course on Physics fundamentals.",
                timeLabel = "Today"
            ),
            Announcement(
                title = "System Maintenance",
                message = "The app will be down for maintenance on Sunday.",
                timeLabel = "Yesterday"
            )
        )
        anns.adapter = AnnouncementAdapter(items = annItems, onClick = {
            // TODO: open announcement detail
        })

        return view
    }
}
