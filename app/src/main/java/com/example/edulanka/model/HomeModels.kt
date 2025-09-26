package com.example.edulanka.model

data class FeaturedLesson(
    val title: String,
    val imageRes: Int
)

data class Announcement(
    val title: String,
    val message: String,
    val timeLabel: String
)
