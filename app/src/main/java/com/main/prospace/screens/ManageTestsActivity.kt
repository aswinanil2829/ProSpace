package com.main.prospace.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.main.prospace.database.AppdataBase
import com.main.prospace.databinding.ActivityManageTestsBinding

class ManageTestsActivity : AppCompatActivity() {
    lateinit var binding: ActivityManageTestsBinding

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).userDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageTestsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.llAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        binding.llAddTest.setOnClickListener {
            val intent = Intent(this, AddTestActivity::class.java)
            startActivity(intent)
        }

        binding.llTaskList.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }

        binding.llTestList.setOnClickListener {
            val intent = Intent(this, TestListActivity::class.java)
            startActivity(intent)
        }
    }
}