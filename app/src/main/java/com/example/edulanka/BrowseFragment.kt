package com.example.edulanka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edulanka.home.LessonsAdapter
import com.example.edulanka.model.Lesson
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.text.Editable
import android.text.TextWatcher
import android.content.Intent

class BrowseFragment : Fragment() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: LessonsAdapter
    private val allLessons = mutableListOf<Lesson>()
    private var subjectFilter: String = "All"
    private var isLecturer: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_browse, container, false)

        // Determine role from parent activity extras
        val role = activity?.intent?.getStringExtra("ROLE") ?: ""
        isLecturer = role.equals("Lecturer", true) || role.equals("Lecture", true)

        // RecyclerView setup
        rv = view.findViewById(R.id.rvLessons)
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = LessonsAdapter(emptyList()) { lesson ->
            val i = Intent(requireContext(), LessonDetailActivity::class.java)
            i.putExtra("TITLE", lesson.title)
            i.putExtra("VIDEO_URL", lesson.videoUrl)
            startActivity(i)
        }
        rv.adapter = adapter

        // Search
        val inputSearch: TextInputEditText = view.findViewById(R.id.inputSearch)
        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { applyFilters() }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Subject chips
        val chipGroup: ChipGroup = view.findViewById(R.id.chipGroup)
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            subjectFilter = when (checkedId) {
                R.id.chipMath -> "Mathematics"
                R.id.chipScience -> "Science"
                R.id.chipEnglish -> "English"
                R.id.chipHistory -> "History"
                else -> "All"
            }
            applyFilters()
        }

        // Lecturer-only FAB
        val fab: FloatingActionButton = view.findViewById(R.id.fabAddLesson)
        if (isLecturer) {
            fab.visibility = View.VISIBLE
            fab.setOnClickListener {
                val email = activity?.intent?.getStringExtra("EMAIL")
                val i = Intent(requireContext(), CreateLessonActivity::class.java)
                i.putExtra("ROLE", role)
                if (email != null) i.putExtra("EMAIL", email)
                startActivity(i)
            }
        } else {
            fab.visibility = View.GONE
        }

        subscribeLessons()
        return view
    }

    private fun subscribeLessons() {
        FirebaseFirestore.getInstance()
            .collection("lessons")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { qs, _ ->
                if (qs == null) return@addSnapshotListener
                allLessons.clear()
                for (doc in qs.documents) {
                    val lesson = Lesson(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        subject = doc.getString("subject") ?: "",
                        grade = doc.getString("grade") ?: "",
                        videoUrl = doc.getString("videoUrl") ?: "",
                        thumbUrl = doc.getString("thumbUrl") ?: "",
                        createdByUid = doc.getString("createdByUid")
                    )
                    allLessons.add(lesson)
                }
                applyFilters()
            }
    }

    private fun applyFilters() {
        val query = view?.findViewById<TextInputEditText>(R.id.inputSearch)?.text?.toString()?.trim()?.lowercase()
        val filtered = allLessons.filter { l ->
            val subjectOk = subjectFilter == "All" || l.subject.equals(subjectFilter, true)
            val searchOk = query.isNullOrEmpty() || l.title.lowercase().contains(query)
            subjectOk && searchOk
        }
        adapter.update(filtered)
    }
}
