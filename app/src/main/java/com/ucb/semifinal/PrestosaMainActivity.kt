package com.ucb.semifinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ucb.semifinal.databinding.PrestosaActivityMainBinding
import com.ucb.semifinal.recycleradapters.CourseDetailAdapter
import com.ucb.semifinal.rooms.databaseconfig.AppDatabase
import com.ucb.semifinal.rooms.roomdao.CourseDetailDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrestosaMainActivity : AppCompatActivity() {

    private lateinit var binding: PrestosaActivityMainBinding
    private lateinit var courseDetailAdapter: CourseDetailAdapter
    private lateinit var courseDetailDao: CourseDetailDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrestosaActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        setUpRecyclerView()

        // Set OnClickListener for FloatingActionButton
        binding.addGradeButton.setOnClickListener {
            val intent = Intent(this, PrestosaAddCourseDetailActivity::class.java)
            startActivity(intent)
        }

        // Fetch and display data from the database
        fetchDataAndUpdateRecyclerView()
    }

    private fun setUpRecyclerView() {
        // Create and set layout manager
        binding.gradesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize database and DAO
        val database = AppDatabase.getDatabase(this)
        courseDetailDao = database.courseDetailDao()

        // Initialize adapter
        courseDetailAdapter = CourseDetailAdapter(this, lifecycleScope, courseDetailDao)

        // Set adapter to RecyclerView
        binding.gradesRecyclerView.adapter = courseDetailAdapter
    }

    private fun fetchDataAndUpdateRecyclerView() {
        // Asynchronously fetch data from database
        lifecycleScope.launch {
            try {
                val courseDetails = withContext(Dispatchers.IO) {
                    courseDetailDao.getAllCourseDetails()
                }
                // Update RecyclerView with fetched data on the main thread
                courseDetailAdapter.submitList(courseDetails)
            } catch (e: Exception) {
                // Handle exceptions, e.g., show error message
                e.printStackTrace()
            }
        }
    }
}
