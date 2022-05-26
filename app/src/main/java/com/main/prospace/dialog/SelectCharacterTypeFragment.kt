package com.main.prospace.dialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.main.prospace.R
import com.main.prospace.database.AppdataBase
import com.main.prospace.database.model.UserDetailsModel
import com.main.prospace.databinding.FragmentSelectCharacterTypeBinding
import com.main.prospace.helpers.CommonUtils
import com.main.prospace.helpers.Preferences
import com.main.prospace.screens.UserHomeActivity


class SelectCharacterTypeFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSelectCharacterTypeBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(requireContext()).userDetails()
    }

    var selectedCharacterType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheetInternal = d.findViewById<View>(R.id.design_bottom_sheet)
            if (bottomSheetInternal != null) {
                BottomSheetBehavior.from(bottomSheetInternal).state =
                    BottomSheetBehavior.STATE_EXPANDED
            }
            d.setCancelable(false)
        }
        binding = FragmentSelectCharacterTypeBinding.inflate(inflater, container, false)
        preferences = Preferences(requireContext())
        characterTypeList()

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

        binding.tvContinue.setOnClickListener {
            continueToHomeScreen()
        }

        return binding.root
    }

    private fun continueToHomeScreen() {
        if (isEveryFieldValid()) {
            UserDetailsModel(
                uname = preferences.getUserName().toString(),
                email = "",
                characterType = selectedCharacterType,
                password = "",
            )

            preferences.saveCharacterType(characterType = selectedCharacterType)

            val intent = Intent(requireContext(), UserHomeActivity::class.java)
            startActivity(intent)
            dismiss()
        }
    }

    private fun isEveryFieldValid(): Boolean {
        var isValid = true
        if (selectedCharacterType == CommonUtils.SELECT_CHARACTER_TYPE) {
            isValid = false
            Toast.makeText(requireContext(), "Please select a character type", Toast.LENGTH_SHORT)
                .show()
        }
        return isValid
    }

    private fun characterTypeList() {
        val arraySpinner = CommonUtils.CHARACTER_TYPE_ARRAY
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCharacterType.adapter = adapter
    }
}