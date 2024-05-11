package com.ucb.semifinal.recycleradapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.ucb.semifinal.PrestosaDisplayCourseDetailActivity
import com.ucb.semifinal.PrestosaUpdateCourseDetailActivity
import com.ucb.semifinal.R
import com.ucb.semifinal.databinding.PrestosaActivityMainBinding
import com.ucb.semifinal.rooms.roomdao.CourseDetailDao
import com.ucb.semifinal.rooms.roommodel.CourseDetail
import kotlinx.coroutines.launch

class CourseDetailAdapter(
    private val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val courseDetailDao: CourseDetailDao,
    private val binding: PrestosaActivityMainBinding
) : RecyclerView.Adapter<CourseDetailAdapter.CourseDetailViewHolder>() {

    private var courseDetails: MutableList<CourseDetail> = mutableListOf()

    inner class CourseDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener, View.OnClickListener {
        private val edpCodeTextView: TextView = itemView.findViewById(R.id.edpCodeTextView)
        private val courseNameTextView: TextView = itemView.findViewById(R.id.courseNameTextView)
        private val gradeTextView: TextView = itemView.findViewById(R.id.gradeTextView)

        init {
            itemView.setOnLongClickListener(this)
            itemView.setOnClickListener(this)
        }

        fun bind(courseDetail: CourseDetail) {
            edpCodeTextView.text = courseDetail.edpCode.toString()
            courseNameTextView.text = courseDetail.courseName
            gradeTextView.text = courseDetail.grade.toString()

            // Update background color based on grade
            if (courseDetail.grade > 3.0) {
                itemView.setBackgroundResource(R.color.colorFailed)
            } else {
                itemView.setBackgroundResource(R.color.colorPassed)
            }
        }

        override fun onClick(v: View?) {
            // Handle item click event here
            val courseDetail = courseDetails[adapterPosition]
            val intent = Intent(itemView.context, PrestosaDisplayCourseDetailActivity::class.java)

            // Pass the course detail data to the intent individually
            intent.putExtra("edpCode", courseDetail.edpCode)
            intent.putExtra("courseName", courseDetail.courseName)
            intent.putExtra("time", courseDetail.time)
            intent.putExtra("grade", courseDetail.grade)
            // Before putting into the intent
            Log.d("GradeTag", "Grade value before putting into intent: ${courseDetail.grade}")
            // Start the activity
            itemView.context.startActivity(intent)
        }

        override fun onLongClick(v: View?): Boolean {
            // Show options dialog
            showOptionsDialog(adapterPosition)
            return true
        }

        private fun showOptionsDialog(position: Int) {
            val options = arrayOf("Update", "Delete")

            val builder = AlertDialog.Builder(context) // Use context from the adapter
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        // Start the update activity
                        val intent = Intent(context, PrestosaUpdateCourseDetailActivity::class.java)
                        intent.putExtra("edpCode", courseDetails[position].edpCode)
                        intent.putExtra("courseName", courseDetails[position].courseName)
                        intent.putExtra("time", courseDetails[position].time)
                        intent.putExtra("grade", courseDetails[position].grade)
                        // Assuming you have a way to get the courseId for the course detail at the given position
                        val courseId = courseDetails[position].id // Adjust this line based on how you access the courseId
                        intent.putExtra("courseId", courseId) // Pass the courseId
                        context.startActivity(intent)
                    }
                    1 -> {
                        // Delete action
                        deleteCourseDetail(position)
                    }
                }
            }
            builder.create().show()
        }



        private fun deleteCourseDetail(position: Int) {
            val courseDetail = courseDetails[position]
            // Remove item from the list
            courseDetails.removeAt(position)
            // Notify adapter of the item removal
            notifyItemRemoved(position)
            // Delete item from the Room database
            lifecycleScope.launch {
                courseDetailDao.deleteCourseDetail(courseDetail)
            }

            // Check if the list is empty
            if (courseDetails.isEmpty()) {
                // Show the noGradesTextView
                binding.noGradesMessage.visibility = View.VISIBLE
            } else {
                // Hide the noGradesTextView
                binding.noGradesMessage.visibility = View.GONE
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseDetailViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_grade, parent, false)
        return CourseDetailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseDetailViewHolder, position: Int) {
        holder.bind(courseDetails[position])
    }

    override fun getItemCount(): Int {
        return courseDetails.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<CourseDetail>) {
        courseDetails.clear()
        courseDetails.addAll(newList)
        notifyDataSetChanged()
        // Check if the list is empty
        if (courseDetails.isEmpty()) {
            // Show the noGradesTextView
            binding.noGradesMessage.visibility = View.VISIBLE
        } else {
            // Hide the noGradesTextView
            binding.noGradesMessage.visibility = View.GONE
        }

    }
}
