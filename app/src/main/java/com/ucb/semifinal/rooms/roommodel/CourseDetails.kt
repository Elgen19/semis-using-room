package com.ucb.semifinal.rooms.roommodel

import androidx.room.Entity
import androidx.room.PrimaryKey

// CourseDetail.kt
@Entity(tableName = "course_details")
data class CourseDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val edpCode: Int,
    val courseName: String,
    val time: String,
    val grade: Double
)

