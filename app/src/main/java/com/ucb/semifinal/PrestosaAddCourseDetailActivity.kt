package com.ucb.semifinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ucb.semifinal.databinding.ActivityPrestosaAddCourseDetailBinding
import com.ucb.semifinal.rooms.databaseconfig.AppDatabase
import com.ucb.semifinal.rooms.roommodel.CourseDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrestosaAddCourseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrestosaAddCourseDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrestosaAddCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database
        db = AppDatabase.getDatabase(applicationContext)

        // Set click listener for save button
        binding.saveButton.setOnClickListener {
            saveCourseDetail()
        }
    }

    private fun saveCourseDetail() {
        val edpCode = binding.edpCodeEditText.text.toString().toIntOrNull()
        val courseName = binding.courseNameEditText.text.toString()
        val time = binding.timeEditText.text.toString()
        val grade = binding.gradeEditText.text.toString().toDoubleOrNull()

        if (edpCode == null || grade == null) {
            Toast.makeText(this, "Invalid EDP Code or Grade", Toast.LENGTH_SHORT).show()
            return
        }

        val courseDetail = CourseDetail(
            edpCode = edpCode,
            courseName = courseName,
            time = time,
            grade = grade
        )

        // Insert course detail into Room database
        lifecycleScope.launch(Dispatchers.IO) {
            db.courseDetailDao().insertCourseDetail(courseDetail)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@PrestosaAddCourseDetailActivity, "Course detail saved", Toast.LENGTH_SHORT).show()
                finish() // Finish activity after saving
            }
        }

    }
}
