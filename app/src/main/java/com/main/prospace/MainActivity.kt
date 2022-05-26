package com.main.prospace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.main.prospace.databinding.ActivityMainBinding
import com.main.prospace.databinding.ActivityWelcomeBinding
import com.main.prospace.screens.SplashActivity
import com.main.prospace.screens.WelcomeActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }
}