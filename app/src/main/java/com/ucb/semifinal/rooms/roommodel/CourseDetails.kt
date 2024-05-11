package com.ucb.semifinal.rooms.roommodel

import androidx.room.Entity
import androidx.room.PrimaryKey

// CourseDetail.kt
@Entity(tableName = "course_details")
data class CourseDetail(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var edpCode: Int,
    var courseName: String,
    var time: String,
    var grade: Double
)

