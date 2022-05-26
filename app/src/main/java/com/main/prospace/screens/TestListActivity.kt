package com.main.prospace.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.main.prospace.adapters.TestGeneratorListAdapter
import com.main.prospace.adapters.TestListAdapter
import com.main.prospace.database.AppdataBase
import com.main.prospace.database.model.TestDetailsModel
import com.main.prospace.databinding.ActivityTestListBinding
import com.main.prospace.helpers.CommonUtils
import com.main.prospace.helpers.Preferences
import com.main.prospace.interfaces.ITestClickListenerListener

class TestListActivity : AppCompatActivity(), ITestClickListenerListener {
    lateinit var binding: ActivityTestListBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).testDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestListBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        preferences = Preferences(this)

        setTaskList()
    }

    private fun setTaskList() {
//        setDummyTaskList()      //load dummy data
        setDBTestList()          //load data added in db
    }

    private fun setTestListAdapter(list: ArrayList<TestDetailsModel>) {
        if (list.isNotEmpty()) {
            val adapter = TestListAdapter(this, list)
            val layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter.clickListener = this
            binding.rvTestList.adapter = adapter
            binding.rvTestList.layoutManager = layoutManager
        } else {
            binding.tvNoDataFound.visibility = View.VISIBLE
        }
    }

    override fun onTestClickListenerListener(item: TestDetailsModel, position: Int) {
        val intent = Intent(this, TestDetailsActivity::class.java)
        intent.putExtra("id", item.tableId)
        startActivity(intent)
    }

    private fun setDBTestList() {
        when (preferences.getUserType()) {
            CommonUtils.ADMIN -> {
                setTestListAdapter(appdataBase.getTestList() as ArrayList<TestDetailsModel>)
            }
        }
    }

    private fun setDummyTestList() {
        val list = ArrayList<TestDetailsModel>()
        list.add(
            TestDetailsModel(
                date = "1/5/2022",
                characterType = "Ambivert",
                test = "How are you feeling today?",
                answerInputFormat = "Seek Bar"
            )
        )
        list.add(
            TestDetailsModel(
                date = "2/5/2022",
                characterType = "Ambivert",
                test = "Tell us about your day",
                answerInputFormat = "Edit Text"
            )
        )
        list.add(
            TestDetailsModel(
                date = "3/5/2022",
                characterType = "Ambivert",
                test = "Does the tasks help you",
                answerInputFormat = "Radio Button"
            )
        )
        list.add(
            TestDetailsModel(
                date = "4/5/2022",
                characterType = "Ambivert",
                test = "Rate the tasks",
                answerInputFormat = "Rating Bar"
            )
        )
        setTestListAdapter(list)
    }

}