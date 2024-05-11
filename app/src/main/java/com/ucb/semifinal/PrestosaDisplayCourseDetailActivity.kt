package com.ucb.semifinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ucb.semifinal.databinding.ActivityPrestosaDisplayCourseDetailBinding
import com.ucb.semifinal.rooms.databaseconfig.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrestosaDisplayCourseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrestosaDisplayCourseDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrestosaDisplayCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database
        db = AppDatabase.getDatabase(applicationContext)

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

        binding.updateButton.setOnClickListener {
            // Query the database to get the id associated with the edpCode
            lifecycleScope.launch(Dispatchers.IO) {
                val courseDetail = db.courseDetailDao().getCourseDetailByEdpCode(edpCode)
                val courseId = courseDetail?.id?: -1L // Handle the case where courseDetail is null
                withContext(Dispatchers.Main) {
                    // Create an intent to start the PrestosaUpdateCourseDetailActivity
                    val intent = Intent(this@PrestosaDisplayCourseDetailActivity, PrestosaUpdateCourseDetailActivity::class.java)

                    // Pass the retrieved data to the update activity using intent extras
                    intent.putExtra("edpCode", edpCode)
                    intent.putExtra("courseName", courseName)
                    intent.putExtra("time", time)
                    intent.putExtra("grade", grade)
                    intent.putExtra("courseId", courseId) // Pass the id

                    // Start the update activity
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.closeButton.setOnClickListener {
            val intent = Intent(this, PrestosaMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish() // Optionally, finish this activity to go back to the previous activity in the stack
        }
    }
}
