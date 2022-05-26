package com.main.prospace.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.main.prospace.adapters.TestGeneratorListAdapter
import com.main.prospace.database.AppdataBase
import com.main.prospace.database.model.TestDetailsModel
import com.main.prospace.databinding.ActivityTestGeneratorBinding
import com.main.prospace.helpers.CommonUtils
import com.main.prospace.helpers.Preferences
import com.main.prospace.interfaces.ITestClickListenerListener
import com.main.prospace.interfaces.ITestGeneratorClickListenerListener

class TestGeneratorActivity : AppCompatActivity(), ITestGeneratorClickListenerListener {
    lateinit var binding: ActivityTestGeneratorBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).testDetails()
    }

    lateinit var adapterGenerator: TestGeneratorListAdapter
    var analyticsRate: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestGeneratorBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        preferences = Preferences(this)

        adapterGenerator = TestGeneratorListAdapter(this, arrayListOf())

        getTestList()

        binding.tvTitle.text = "Hello," + "\n" + preferences.getUserName()

        binding.tvNext.setOnClickListener {
            if (adapterGenerator.itemCount > (binding.vpQuestions.currentItem + 1))
                binding.vpQuestions.currentItem = binding.vpQuestions.currentItem + 1

            Log.d(
                "count-->",
                adapterGenerator.itemCount.toString() + "," + (binding.vpQuestions.currentItem + 1).toString()
            )

            if (adapterGenerator.itemCount == (binding.vpQuestions.currentItem + 1)) {
                binding.tvNext.visibility = View.GONE
                binding.tvFinish.visibility = View.VISIBLE
            }
        }

        binding.tvFinish.setOnClickListener {
            val intent = Intent(this, TestAnalyticsActivity::class.java)
            intent.putExtra("analyticsRate", analyticsRate)
            startActivity(intent)
            finish()
        }
    }

    private fun getTestList() {
        setDBTestList()
    }

    private fun setTestListAdapter(list: ArrayList<TestDetailsModel>) {
        if(list.isNotEmpty()) {
            binding.tvNoDataFound.visibility = View.GONE
            binding.tvNext.visibility = View.VISIBLE

            adapterGenerator = TestGeneratorListAdapter(this, list)
            val layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapterGenerator.clickListener = this
            binding.vpQuestions.adapter = adapterGenerator
        } else {
            binding.tvNoDataFound.visibility = View.VISIBLE
            binding.tvNext.visibility = View.GONE
        }
    }

    private fun setDBTestList() {
        Log.d("characterType--->", preferences.getCharacterType().toString())
        when (preferences.getUserType()) {
            CommonUtils.ADMIN -> {
                setTestListAdapter(appdataBase.getTestList() as ArrayList<TestDetailsModel>)
            }
            CommonUtils.USER -> {
                setTestListAdapter(appdataBase.getTestListWithCharacter(preferences.getCharacterType()!!) as ArrayList<TestDetailsModel>)
            }
        }
    }

    override fun onTestGeneratorClickListenerListener(rating: Float, position: Int) {
        Log.d("analyticsRate-->", rating.toString())
        if(position == 0) {
            analyticsRate = rating.toInt()
        }
    }
}