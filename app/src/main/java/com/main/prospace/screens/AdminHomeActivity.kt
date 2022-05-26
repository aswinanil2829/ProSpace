package com.main.prospace.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.main.prospace.R
import com.main.prospace.database.AppdataBase
import com.main.prospace.databinding.ActivityAdminHomeBinding
import com.main.prospace.databinding.ActivityUserSignUpBinding
import com.main.prospace.helpers.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

class AdminHomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminHomeBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).userDetails()
    }

    var backPressed: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        preferences = Preferences(this)

        binding.llManageUsers.setOnClickListener {
            val intent = Intent(this, ManageUsersActivity::class.java)
            startActivity(intent)
        }

        binding.llManageTests.setOnClickListener {
            val intent = Intent(this, ManageTestsActivity::class.java)
            startActivity(intent)
        }

        binding.ivLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage("Do you want to logout?")
            .setNeutralButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Logout") { dialog, which ->
                preferences.saveUserType(userType = "")
                preferences.saveUserId(userId = 0)
                preferences.saveUserName(userName = "")
                preferences.saveCharacterType(characterType = "")

                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }

    override fun onBackPressed() {
        if (backPressed) {
            backPressed = false
            Toast.makeText(
                this, "Press Back again to Exit.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            finish()
        }
    }

}