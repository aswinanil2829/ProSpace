package com.main.prospace.screens

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.main.prospace.R
import com.main.prospace.database.AppdataBase
import com.main.prospace.database.model.TaskDetailsModel
import com.main.prospace.database.model.TestDetailsModel
import com.main.prospace.databinding.ActivityAddTaskBinding
import com.main.prospace.databinding.ActivityAddTestBinding
import com.main.prospace.helpers.CommonUtils
import java.util.*
import java.util.regex.Pattern

class AddTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTestBinding

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).testDetails()
    }

    var selectedCharacterType: String = ""
    var answerInputFormat: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTestBinding.inflate(layoutInflater)
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

        binding.tvAddTest.setOnClickListener {
            addTest()
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

        binding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val rb = group.findViewById<View>(checkedId) as RadioButton
            if (checkedId > -1) {
                answerInputFormat = rb.text.toString()
            }
        })
    }

    private fun addTest() {
        if (isEveryFieldValid()) {
            Toast.makeText(this, "Test added successfully", Toast.LENGTH_SHORT).show()

            appdataBase.insertTestDetails(
                TestDetailsModel(
                    date = binding.tvDate.text.toString(),
                    characterType = selectedCharacterType,
                    test = binding.etTest.text.toString(),
                    answerInputFormat = answerInputFormat
                )
            )
            binding.etTest.setText("")
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
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCharacterType.adapter = adapter
    }

    private fun isEveryFieldValid(): Boolean {
        var isValid = true
        when {
            binding.etTest.text.toString().trim().isValidValue() -> {
                isValid = false
                binding.etTest.error = "Please enter a test question"
                binding.etTest.requestFocus()
            }
        }
        if (selectedCharacterType == CommonUtils.SELECT_CHARACTER_TYPE) {
            isValid = false
            Toast.makeText(this, "Please select a character type", Toast.LENGTH_SHORT).show()
        }

        if (selectedCharacterType.isEmpty()) {
            isValid = false
            Toast.makeText(this, "Please select an answer input format", Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    fun CharSequence.isValidValue(): Boolean =
        Pattern.compile(CommonUtils.REGEX_EMPTY_VALUE).matcher(this.trim()).find()
}