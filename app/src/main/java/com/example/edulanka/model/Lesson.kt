package com.example.edulanka.model

data class Lesson(
    val id: String? = null,
    val title: String = "",
    val subject: String = "",
    val grade: String = "",
    val videoUrl: String = "",
    val thumbUrl: String = "",
    val createdByUid: String? = null
)
