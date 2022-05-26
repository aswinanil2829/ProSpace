package com.main.prospace.screens

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.main.prospace.database.AppdataBase
import com.main.prospace.database.model.TaskDetailsModel
import com.main.prospace.databinding.ActivityAddTaskBinding
import com.main.prospace.helpers.CommonUtils
import java.util.*
import java.util.regex.Pattern


class AddTaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTaskBinding

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).taskDetails()
    }

    var selectedCharacterType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        setCurrentDate()
        characterTypeList()

        binding.ivCalendar.setOnClickListener {
            showDatePicker()
        }

        binding.tvDate.setOnClickListener {
            showDatePicker()
        }

        binding.tvAddTask.setOnClickListener {
            addTask()
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

    private fun setCurrentDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.tvDate.text = (day.toString() + "/" + month.toString() + "/" + year.toString())
    }

    private fun characterTypeList() {
        val arraySpinner = CommonUtils.CHARACTER_TYPE_ARRAY
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCharacterType.adapter = adapter
    }

    private fun addTask() {
        if (isEveryFieldValid()) {
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
            appdataBase.insertTaskDetails(
                TaskDetailsModel(
                    date = binding.tvDate.text.toString(),
                    characterType = selectedCharacterType,
                    task = binding.etTask.text.toString()
                )
            )
            binding.etTask.setText("")
        }
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
        if(selectedCharacterType == CommonUtils.SELECT_CHARACTER_TYPE) {
            isValid = false
            Toast.makeText(this, "Please select a character type", Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    fun CharSequence.isValidValue(): Boolean =
        Pattern.compile(CommonUtils.REGEX_EMPTY_VALUE).matcher(this.trim()).find()
}