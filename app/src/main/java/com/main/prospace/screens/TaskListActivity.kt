package com.main.prospace.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.main.prospace.R
import com.main.prospace.adapters.TaskListAdapter
import com.main.prospace.adapters.UserListAdapter
import com.main.prospace.database.AppdataBase
import com.main.prospace.database.model.TaskDetailsModel
import com.main.prospace.database.model.TestDetailsModel
import com.main.prospace.database.model.UserDetailsModel
import com.main.prospace.databinding.ActivityTaskListBinding
import com.main.prospace.databinding.ActivityUserListBinding
import com.main.prospace.helpers.CommonUtils
import com.main.prospace.helpers.Preferences
import com.main.prospace.interfaces.ITaskClickListenerListener

class TaskListActivity : AppCompatActivity(), ITaskClickListenerListener {
    lateinit var binding: ActivityTaskListBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).taskDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        preferences = Preferences(this)

        setTaskList()
    }

    private fun setTaskList() {
        setDBTestList()
    }

    private fun setTaskListAdapter(list: ArrayList<TaskDetailsModel>) {
        if (list.isNotEmpty()) {
            val adapter = TaskListAdapter(this, list)
            val layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter.clickListener = this
            binding.rvTaskList.adapter = adapter
            binding.rvTaskList.layoutManager = layoutManager
        } else {
            binding.tvNoDataFound.visibility = View.VISIBLE
        }
    }

    override fun onTaskClickListenerListener(item: TaskDetailsModel, position: Int) {
        val intent = Intent(this, TaskDetailsActivity::class.java)
        intent.putExtra("id", item.tableId)
        startActivity(intent)
    }

    private fun setDBTestList() {
        when (preferences.getUserType()) {
            CommonUtils.ADMIN -> {
                setTaskListAdapter(appdataBase.getTaskList() as ArrayList<TaskDetailsModel>)
            }
            CommonUtils.USER -> {
                setTaskListAdapter(appdataBase.getTaskListWithCharacter(preferences.getCharacterType()!!) as ArrayList<TaskDetailsModel>)
            }
        }
    }

}