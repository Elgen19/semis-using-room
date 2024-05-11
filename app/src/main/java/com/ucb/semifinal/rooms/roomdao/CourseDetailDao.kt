package com.ucb.semifinal.rooms.roomdao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ucb.semifinal.rooms.roommodel.CourseDetail

@Dao
interface CourseDetailDao {
    @Insert
    suspend fun insertCourseDetail(courseDetail: CourseDetail)

    @Query("SELECT * FROM course_details")
    suspend fun getAllCourseDetails(): List<CourseDetail>

    @Delete
    suspend fun deleteCourseDetail(courseDetail: CourseDetail)

    @Query("SELECT * FROM course_details WHERE id = :id")
    suspend fun getCourseDetailById(id: Long): CourseDetail?

    @Update
    suspend fun updateCourseDetail(courseDetail: CourseDetail)

    // Custom query to get a course detail by its edpCode
    @Query("SELECT * FROM course_details WHERE edpCode = :edpCode")
    suspend fun getCourseDetailByEdpCode(edpCode: Int): CourseDetail?
}
