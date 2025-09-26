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
            startActivity(Intent(requireContext(), AnnouncementsActivity::class.java))
        }

        // Announcements
        val anns: RecyclerView = view.findViewById(R.id.rvAnnouncements)
        anns.layoutManager = LinearLayoutManager(requireContext())
        val annItems = listOf(
            Announcement("New Science Course Available", "Explore our new course on Physics fundamentals.", "Today"),
            Announcement("System Maintenance", "The app will be down for maintenance on Sunday.", "Yesterday")
        )
        anns.adapter = AnnouncementAdapter(annItems) {
            // TODO: open announcement detail
        }

        return view
    }
}
