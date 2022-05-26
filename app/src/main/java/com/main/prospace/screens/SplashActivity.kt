package com.main.prospace.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.main.prospace.databinding.ActivitySplashBinding
import com.main.prospace.helpers.CommonUtils
import com.main.prospace.helpers.Preferences

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        preferences = Preferences(this)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            when (preferences.getUserType()) {
                CommonUtils.ADMIN -> {
                    val intent = Intent(this, AdminHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                CommonUtils.USER -> {
                    val intent = Intent(this, UserHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)
    }
}