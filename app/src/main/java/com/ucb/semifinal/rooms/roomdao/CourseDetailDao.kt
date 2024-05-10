package com.ucb.semifinal.rooms.roomdao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ucb.semifinal.rooms.roommodel.CourseDetail

@Dao
interface CourseDetailDao {
    @Insert
    suspend fun insertCourseDetail(courseDetail: CourseDetail)

    @Query("SELECT * FROM course_details")
    suspend fun getAllCourseDetails(): List<CourseDetail>

    @Delete
    suspend fun deleteCourseDetail(courseDetail: CourseDetail)

    // Add more methods as needed, such as querying or deleting course details
}
