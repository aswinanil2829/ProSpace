package com.main.prospace.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.main.prospace.R
import com.main.prospace.database.AppdataBase
import com.main.prospace.databinding.ActivityUserHomeBinding
import com.main.prospace.helpers.Preferences


class UserHomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserHomeBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).userDetails()
    }

    var backPressed: Boolean = true

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        preferences = Preferences(this)

        googleSignOutInitialization()

        binding.tvTitle.text = "Hello," + "\n" + preferences.getUserName()

        binding.tvCheckTask.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }

        binding.tvTakeSurvey.setOnClickListener {
            val intent = Intent(this, TestGeneratorActivity::class.java)
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

                googleLogout()

                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun googleLogout() {
        mGoogleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
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
            finishAffinity()
        }
    }

    private fun googleSignOutInitialization() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }
}