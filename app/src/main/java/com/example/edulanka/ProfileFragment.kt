package com.example.edulanka

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val role = activity?.intent?.getStringExtra("ROLE") ?: "Student"
        val email = activity?.intent?.getStringExtra("EMAIL") ?: ""

        val btnOpen: Button = view.findViewById(R.id.btnOpenProfile)
        btnOpen.setOnClickListener {
            val intent = if (role.equals("Lecturer", true) || role.equals("Lecture", true))
                Intent(requireContext(), LecturerProfileActivity::class.java)
            else
                Intent(requireContext(), StudentProfileActivity::class.java)

            intent.putExtra("EMAIL", email)
            intent.putExtra("NAME", "User Name")
            startActivity(intent)
        }
        return view
    }
}
