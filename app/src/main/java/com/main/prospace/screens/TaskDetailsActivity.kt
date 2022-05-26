package com.main.prospace.screens

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.main.prospace.database.AppdataBase
import com.main.prospace.database.model.TaskDetailsModel
import com.main.prospace.databinding.ActivityTaskDetailsBinding
import com.main.prospace.helpers.CommonUtils
import com.main.prospace.helpers.Preferences
import java.util.*
import java.util.regex.Pattern

class TaskDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTaskDetailsBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).taskDetails()
    }

    var selectedCharacterType: String = ""

    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        preferences = Preferences(this)

        val bundle: Bundle? = intent.extras
        id = bundle!!.getInt("id")
        getTaskDetails(id = id)

        characterTypeList()
        setUserType()
        setSelectedCharaterType()

        binding.ivCalendar.setOnClickListener {
            showDatePicker()
        }

        binding.tvDate.setOnClickListener {
            showDatePicker()
        }

        binding.tvUpdate.setOnClickListener {
            update()
        }

        binding.tvDelete.setOnClickListener {
            delete()
        }

        binding.spCharacterType?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCharacterType = binding.spCharacterType.selectedItem.toString()
                }
            }

    }

    private fun setSelectedCharaterType() {
        when (selectedCharacterType) {
            CommonUtils.AMBIVERT -> {
                binding.spCharacterType.setSelection(1)
                selectedCharacterType = CommonUtils.AMBIVERT
            }
            CommonUtils.INTROVERT -> {
                binding.spCharacterType.setSelection(2)
                selectedCharacterType = CommonUtils.INTROVERT
            }
            CommonUtils.EXTROVERT -> {
                binding.spCharacterType.setSelection(3)
                selectedCharacterType = CommonUtils.EXTROVERT
            }
        }
    }

    private fun setUserType() {
        when (preferences.getUserType()) {
            CommonUtils.ADMIN -> {
                binding.llEdit.visibility = View.VISIBLE
            }
            CommonUtils.USER -> {
                binding.tvTitle.text = "Task Details"
                binding.tvSubTitle.text = "Complete each daily tasks to improve mental health"

                binding.llEdit.visibility = View.GONE
                binding.llCharacterType.visibility = View.GONE
                binding.etTask.isEnabled = false
                binding.ivCalendar.isEnabled = false
                binding.tvDate.isEnabled = false
            }
        }
    }

    private fun getTaskDetails(id: Int) {
        val it = appdataBase.getTaskDetails(id = id)
        binding.tvDate.text = it.date
        selectedCharacterType = it.characterType
        binding.etTask.setText(it.task)
    }

    private fun update() {
        if (isEveryFieldValid()) {
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()
            appdataBase.updateTaskDetails(
                id = id,
                date = binding.tvDate.text.toString(),
                characterType = selectedCharacterType,
                task = binding.etTask.text.toString()
            )
            finish()
        }
    }

    private fun delete() {
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
        appdataBase.deleteTaskDetails(
            id = id
        )
        finish()
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                binding.tvDate.text = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    fun characterTypeList() {
        val arraySpinner = CommonUtils.CHARACTER_TYPE_ARRAY
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCharacterType.adapter = adapter
    }

    private fun isEveryFieldValid(): Boolean {
        var isValid = true
        when {
            binding.etTask.text.toString().trim().isValidValue() -> {
                isValid = false
                binding.etTask.error = "Please enter a task"
                binding.etTask.requestFocus()
            }
        }
        if (selectedCharacterType == CommonUtils.SELECT_CHARACTER_TYPE) {
            isValid = false
            Toast.makeText(this, "Please select a character type", Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    fun CharSequence.isValidValue(): Boolean =
        Pattern.compile(CommonUtils.REGEX_EMPTY_VALUE).matcher(this.trim()).find()
}