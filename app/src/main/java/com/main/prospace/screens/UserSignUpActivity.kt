package com.main.prospace.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.main.prospace.database.model.UserDetailsModel
import com.main.prospace.databinding.ActivityUserSignUpBinding
import com.main.prospace.helpers.CommonUtils
import java.util.regex.Pattern


class UserSignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserSignUpBinding

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).userDetails()
    }

    var selectedCharacterType: String = ""

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSignUpBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        characterTypeList()
        googleSignInInitialization()

        binding.ivWelcomeHome.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvRegister.setOnClickListener {
            signUp()
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

        binding.inOtherLogin.ivGoogleSignIn.setOnClickListener {
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

        binding.inOtherLogin.ivGoogleSignIn.setOnClickListener {
            val intent = Intent(this, OtherSignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun signUp() {
        if (isEveryFieldValid()) {
            appdataBase.insertUserDetails(
                UserDetailsModel(
                    uname = binding.etUserName.text.toString(),
                    email = binding.etEmail.text.toString(),
                    characterType = selectedCharacterType,
                    password = binding.etPassword.text.toString(),
                )
            )
            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, UserSignInActivity::class.java)
            startActivity(intent)
            finish()
        }
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
            !binding.etUserName.text.toString().trim().isValidName() -> {
                isValid = false
                binding.etUserName.error = "Please enter a valid name"
                binding.etUserName.requestFocus()
            }
            !binding.etEmail.text.toString().trim().isValidEmail() -> {
                isValid = false
                binding.etEmail.error = "Please enter a valid email"
                binding.etEmail.requestFocus()
            }
            !binding.etPassword.text.toString().trim().isValidPassword() -> {
                isValid = false
                binding.etPassword.error =
                    "Password must contain six characters, at least one letter and one number"
                binding.etPassword.requestFocus()
            }
        }
        if (binding.etPassword.text.toString().trim() != binding.etConfirmPassword.text.toString().trim()) {
            isValid = false
            binding.etConfirmPassword.error = "Passwords did not match"
            binding.etConfirmPassword.requestFocus()
        }
        if(selectedCharacterType == CommonUtils.SELECT_CHARACTER_TYPE) {
            isValid = false
            Toast.makeText(this, "Please select a character type", Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    fun CharSequence.isValidName(): Boolean =
        Pattern.compile(CommonUtils.REGEX_NAME_VALIDATOR).matcher(this.trim()).find()

    fun CharSequence.isValidEmail(): Boolean =
        Pattern.compile(CommonUtils.REGEX_EMAIL_VALIDATOR).matcher(this.trim()).find()

    fun CharSequence.isValidPassword(): Boolean =
        Pattern.compile(CommonUtils.REGEX_PASSWORD_VALIDATOR).matcher(this.trim()).find()

    // onActivityResult() function : this is where
    // we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun googleSignInInitialization() {
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // this is where we update the UI after Google signin takes place
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                SavedPreference.setEmail(this, account.email.toString())
//                SavedPreference.setUsername(this, account.displayName.toString())
//                val intent = Intent(this, DashboardActivity::class.java)
//                startActivity(intent)
//                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
//            startActivity(
////                Intent(
////                    this, DashboardActivity
////                    ::class.java
////                )
//            )
            finish()
        }
    }
}