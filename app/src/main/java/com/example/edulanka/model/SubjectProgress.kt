package com.example.edulanka.model

data class SubjectProgress(
    val subject: String,
    val watched: Int,
    val total: Int
) {
    // Percent equals watch count (simple mode), capped at 100
    val percent: Int get() = if (watched < 0) 0 else if (watched > 100) 100 else watched
}
