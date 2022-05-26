package com.main.prospace.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.main.prospace.databinding.ActivityTestAnalyticsBinding


class TestAnalyticsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestAnalyticsBinding

    var analyticsRate: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestAnalyticsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        val bundle: Bundle? = intent.extras
        analyticsRate = bundle!!.getInt("analyticsRate")
        setLayout()

        binding.llContact1.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + binding.tvContactNo1.text.toString())
            startActivity(intent)
        }

        binding.llContact2.setOnClickListener {
            val intent = Intent(this, UserHomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.llContact2.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + binding.tvContactNo2.text.toString())
            startActivity(intent)
        }

        binding.llEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:" + binding.tvEmail)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Need Professional Help")
            startActivity(intent)

        }

        binding.tvDone.setOnClickListener {
            val intent = Intent(this, UserHomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvClose.setOnClickListener {
            val intent = Intent(this, UserHomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setLayout() {
        Log.d("analyticsRate-->", analyticsRate.toString())
        if (analyticsRate > 5) {
            binding.llThankYou.visibility = View.VISIBLE
            binding.llProfessionalHelp.visibility = View.GONE
        } else {
            binding.llThankYou.visibility = View.GONE
            binding.llProfessionalHelp.visibility = View.VISIBLE
        }
    }
}