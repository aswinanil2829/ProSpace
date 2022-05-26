package com.main.prospace.screens

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.main.prospace.R
import com.main.prospace.database.AppdataBase
import com.main.prospace.databinding.ActivityTaskDetailsBinding
import com.main.prospace.databinding.ActivityTestDetailsBinding
import com.main.prospace.helpers.CommonUtils
import com.main.prospace.helpers.Preferences
import java.util.*
import java.util.regex.Pattern

class TestDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestDetailsBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).testDetails()
    }

    var selectedCharacterType: String = ""
    var answerInputFormat: String = ""

    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestDetailsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        preferences = Preferences(this)

        val bundle: Bundle? = intent.extras
        id = bundle!!.getInt("id")
        getTaskDetails(id = id)

        characterTypeList()
        setUserType()
        setSelectedCharacterType()

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

        binding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val rb = group.findViewById<View>(checkedId) as RadioButton
            if (checkedId > -1) {
                answerInputFormat = rb.text.toString()
            }
        })
    }

    private fun setSelectedCharacterType() {
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
                binding.etTest.isEnabled = false
                binding.ivCalendar.isEnabled = false
                binding.tvDate.isEnabled = false
            }
        }
    }

    private fun getTaskDetails(id: Int) {
        val it = appdataBase.getTestDetails(id = id)
        binding.tvDate.text = it.date
        selectedCharacterType = it.characterType
        answerInputFormat = it.answerInputFormat
        binding.etTest.setText(it.test)
        selectedAnswerInputMethod()
    }

    private fun update() {
        if (isEveryFieldValid()) {
            Toast.makeText(this, "Test updated successfully", Toast.LENGTH_SHORT).show()
            appdataBase.updateTestDetails(
                id = id,
                date = binding.tvDate.text.toString(),
                characterType = selectedCharacterType,
                task = binding.etTest.text.toString(),
                answerInputFormat = answerInputFormat
            )
            finish()
        }
    }

    private fun delete() {
        Toast.makeText(this, "Test deleted", Toast.LENGTH_SHORT).show()
        appdataBase.deleteTestDetails(
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
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCharacterType.adapter = adapter
    }

    private fun selectedAnswerInputMethod() {
        when (answerInputFormat) {
            CommonUtils.EDIT_TEXT -> {
                binding.rbEditText.isChecked = true
            }
            CommonUtils.RATING_BAR -> {
                binding.rbRatingBar.isChecked = true
            }
            CommonUtils.SEEK_BAR -> {
                binding.rbSeekBar.isChecked = true
            }
            CommonUtils.RADIO_BUTTON -> {
                binding.rbRadioButton.isChecked = true
            }
        }
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