package com.main.prospace.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.main.prospace.databinding.ActivityOtherSignInBinding
import com.main.prospace.databinding.ActivityUserSignInBinding
import com.main.prospace.dialog.SelectCharacterTypeFragment
import com.main.prospace.helpers.CommonUtils
import com.main.prospace.helpers.Preferences

class OtherSignInActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtherSignInBinding
    lateinit var preferences: Preferences

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).userDetails()
    }

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var selectCharacterTypeDialog: SelectCharacterTypeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherSignInBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        preferences = Preferences(this)
        googleSignInInitialization()
    }

    /**GOOGLE SIGN IN-----*/
    private fun googleSignInInitialization() {
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        signInGoogle()
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    // onActivityResult() function : this is where
    // we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Log.d("error-->", e.toString())
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // this is where we update the UI after Google signin takes place
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                preferences.saveUserType(userType = CommonUtils.USER)
                preferences.saveUserName(userName = account.displayName.toString())
                preferences.saveUserEmail(userEmail = account.email.toString())

                selectCharacterTypeDialog = SelectCharacterTypeFragment()
                selectCharacterTypeDialog.show(supportFragmentManager, "filter")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(
                Intent(
                    this, UserHomeActivity
                    ::class.java
                )
            )
            finish()
        }
    }
    /**GOOGLE SIGN IN-----*/
}