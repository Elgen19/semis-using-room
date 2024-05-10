package com.ucb.semifinal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ucb.semifinal.databinding.ActivityPrestosaDisplayCourseDetailBinding

class PrestosaDisplayCourseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrestosaDisplayCourseDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrestosaDisplayCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve intent extras
        val edpCode = intent.getIntExtra("edpCode", 0)
        val courseName = intent.getStringExtra("courseName")
        val time = intent.getStringExtra("time")
        val grade = intent.getDoubleExtra("grade", 0.0)
// After retrieving in PrestosaDisplayCourseDetailActivity
        Log.d("GradeTag", "Grade value after retrieving from intent: $grade")
        // Display the retrieved data
        binding.edpCodeTextView.text = edpCode.toString()
        binding.courseNameTextView.text = courseName
        binding.timeTextView.text = time
        binding.gradeTextView.text = grade.toString()
    }
}
