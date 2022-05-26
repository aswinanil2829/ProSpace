package com.main.prospace.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.main.prospace.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, UserSignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvRegister.setOnClickListener {
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
}