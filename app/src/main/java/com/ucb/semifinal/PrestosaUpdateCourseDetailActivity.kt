package com.ucb.semifinal

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ucb.semifinal.databinding.ActivityPrestosaUpdateCourseDetailBinding
import com.ucb.semifinal.rooms.databaseconfig.AppDatabase
import com.ucb.semifinal.rooms.roommodel.CourseDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrestosaUpdateCourseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrestosaUpdateCourseDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrestosaUpdateCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database
        db = AppDatabase.getDatabase(applicationContext)

        // Retrieve extras from the intent
        val edpCode = intent.getIntExtra("edpCode", 0).toString()
        val courseName = intent.getStringExtra("courseName")
        val time = intent.getStringExtra("time")
        val grade = intent.getDoubleExtra("grade", 0.0)
        val courseId = intent.getLongExtra("courseId", -1) // Retrieve the id

        // Prepopulate EditText fields with the retrieved values
        // Debugging: Log the edpCode to ensure it's not null or empty
        Log.d("PrestosaUpdateCourseDetailActivity", "EDP Code from Intent: $edpCode")
        binding.edpCodeEditText.setText(edpCode)
        binding.courseNameEditText.setText(courseName)
        binding.timeEditText.setText(time)
        binding.gradeEditText.setText(grade.toString())

        if (grade in 1.0..3.0) {
            binding.screen.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPassed))
        } else {
            binding.screen.setBackgroundColor(ContextCompat.getColor(this, R.color.colorFailed))
        }


        // Add text changed listener to the grade EditText
        binding.gradeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Parse the grade value from the EditText
                val gradeValue = s.toString().toDoubleOrNull()
                if (gradeValue!= null) {
                    // Check if the grade is within the pass or fail range
                    if (gradeValue in 1.0..3.0) {
                        binding.screen.setBackgroundColor(ContextCompat.getColor(this@PrestosaUpdateCourseDetailActivity, R.color.colorPassed))
                    } else {
                        binding.screen.setBackgroundColor(ContextCompat.getColor(this@PrestosaUpdateCourseDetailActivity, R.color.colorFailed))
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        // Set click listener for the save button
        binding.saveButton.setOnClickListener {
            saveUpdatedCourseDetail(courseId)
        }

        binding.closeButton.setOnClickListener {
            val intent = Intent(this, PrestosaMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish() // Optionally, finish this activity to go back to the previous activity in the stack
        }
    }

    private fun saveUpdatedCourseDetail(courseId: Long) {
        val updatedEdpCode = binding.edpCodeEditText.text.toString().toIntOrNull()
        val updatedCourseName = binding.courseNameEditText.text.toString()
        val updatedTime = binding.timeEditText.text.toString()
        val updatedGrade = binding.gradeEditText.text.toString().toDoubleOrNull()

        if (updatedEdpCode == null || updatedGrade == null) {
            Toast.makeText(this, "Invalid EDP Code or Grade", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedCourseDetail = CourseDetail(
            id = courseId, // Use the courseId passed from the intent
            edpCode = updatedEdpCode,
            courseName = updatedCourseName,
            time = updatedTime,
            grade = updatedGrade
        )

        // Update the course detail in the Room database
        lifecycleScope.launch(Dispatchers.IO) {
            val existingCourseDetail = db.courseDetailDao().getCourseDetailById(courseId)
            existingCourseDetail?.let {
                // Update the existing course detail object with the new values
                it.edpCode = updatedCourseDetail.edpCode
                it.courseName = updatedCourseDetail.courseName
                it.time = updatedCourseDetail.time
                it.grade = updatedCourseDetail.grade
                // No need to set it.id again as it's fetched from the database
                // Call the update method to update the object in the database
                db.courseDetailDao().updateCourseDetail(it)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@PrestosaUpdateCourseDetailActivity, "Course detail updated", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@PrestosaUpdateCourseDetailActivity, PrestosaMainActivity::class.java))
                finish() // Finish activity after updating
            }
        }
    }
}
