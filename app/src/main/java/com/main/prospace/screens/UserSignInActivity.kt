package com.main.prospace.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.main.prospace.R
import com.main.prospace.database.AppdataBase
import com.main.prospace.databinding.ActivityUserSignInBinding
import com.main.prospace.dialog.SelectCharacterTypeFragment
import com.main.prospace.helpers.Preferences
import com.main.prospace.helpers.CommonUtils
import java.util.regex.Pattern

class UserSignInActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserSignInBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).userDetails()
    }

//    lateinit var mGoogleSignInClient: GoogleSignInClient
//    val Req_Code: Int = 123
//    private lateinit var firebaseAuth: FirebaseAuth
//    lateinit var selectCharacterTypeDialog: SelectCharacterTypeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSignInBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        preferences = Preferences(this)

//        googleSignInInitialization()

        binding.ivLeftArrow.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvLogin.setOnClickListener {
            loginCheck()
        }

        binding.tvNotRegistered.setOnClickListener {
            val intent = Intent(this, UserSignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.inOtherLogin.ivGoogleSignIn.setOnClickListener {
            val intent = Intent(this, OtherSignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginCheck() {
        if (binding.etEmail.text.toString() == CommonUtils.ADMIN && binding.etPassword.text.toString() == CommonUtils.ADMIN) {
            preferences.saveUserType(userType = CommonUtils.ADMIN)
            val intent = Intent(this, AdminHomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            if (isEveryFieldValid()) {
                val data = appdataBase.loginCheck(
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString()
                )
                if (data.size > 0) {
                    val userData = appdataBase.getLoggedUser(
                        email = binding.etEmail.text.toString()
                    )

                    preferences.saveUserType(userType = CommonUtils.USER)
                    preferences.saveUserId(userId = userData.tableId)
                    preferences.saveUserName(userName = userData.uname)
                    preferences.saveUserEmail(userEmail = userData.email)
                    preferences.saveCharacterType(characterType = userData.characterType)

                    val intent = Intent(this, UserHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid email id or password", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun isEveryFieldValid(): Boolean {
        var isValid = true
        when {
            !binding.etEmail.text.toString().trim().isValidEmail() -> {
                isValid = false
                binding.etEmail.error = "Please enter a valid email"
                binding.etEmail.requestFocus()
            }
        }
        return isValid
    }

    fun CharSequence.isValidEmail(): Boolean =
        Pattern.compile(CommonUtils.REGEX_EMAIL_VALIDATOR).matcher(this.trim()).find()

//    /**GOOGLE SIGNIN-----*/
//    private fun googleSignInInitialization() {
//        FirebaseApp.initializeApp(this)
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//        firebaseAuth = FirebaseAuth.getInstance()
//    }
//
//    private fun signInGoogle() {
//        val signInIntent: Intent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, Req_Code)
//    }
//
//    // onActivityResult() function : this is where
//    // we provide the task and data for the Google Account
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Req_Code) {
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleResult(task)
//        }
//    }
//
//    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
//            if (account != null) {
//                UpdateUI(account)
//            }
//        } catch (e: ApiException) {
//            Log.d("error-->", e.toString())
//            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // this is where we update the UI after Google signin takes place
//    private fun UpdateUI(account: GoogleSignInAccount) {
//        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                preferences.saveUserType(userType = CommonUtils.USER)
//                preferences.saveUserName(userName = account.displayName.toString())
//                preferences.saveUserEmail(userEmail = account.email.toString())
//
//                selectCharacterTypeDialog = SelectCharacterTypeFragment()
//                selectCharacterTypeDialog.show(supportFragmentManager, "filter")
//            }
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
//            startActivity(
//                Intent(
//                    this, UserHomeActivity
//                    ::class.java
//                )
//            )
//            finish()
//        }
//    }
//    /**GOOGLE SIGNIN-----*/
}