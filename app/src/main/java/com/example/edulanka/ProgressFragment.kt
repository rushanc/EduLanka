package com.example.edulanka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edulanka.home.SubjectProgressAdapter
import com.example.edulanka.model.SubjectProgress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ProgressFragment : Fragment() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: SubjectProgressAdapter
    private var registration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_progress, container, false)
        rv = view.findViewById(R.id.rvSubjectProgress)
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SubjectProgressAdapter(emptyList())
        rv.adapter = adapter

        subscribeProgress()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        registration?.remove()
    }

    private fun subscribeProgress() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val docRef = FirebaseFirestore.getInstance().collection("userProgress").document(uid)
        registration = docRef.addSnapshotListener { snap, _ ->
            val math = (snap?.getLong("mathCount") ?: 0L).toInt()
            val science = (snap?.getLong("scienceCount") ?: 0L).toInt()
            val english = (snap?.getLong("englishCount") ?: 0L).toInt()
            val history = (snap?.getLong("historyCount") ?: 0L).toInt()

            val items = listOf(
                SubjectProgress("Mathematics", watched = math, total = 0),
                SubjectProgress("Science", watched = science, total = 0),
                SubjectProgress("English", watched = english, total = 0),
                SubjectProgress("History", watched = history, total = 0),
            )
            adapter.update(items)
        }
    }
}
