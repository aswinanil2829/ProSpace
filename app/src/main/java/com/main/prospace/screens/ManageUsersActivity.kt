package com.main.prospace.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.main.prospace.R
import com.main.prospace.database.AppdataBase
import com.main.prospace.databinding.ActivityAdminHomeBinding
import com.main.prospace.databinding.ActivityManageUsersBinding

class ManageUsersActivity : AppCompatActivity() {
    lateinit var binding: ActivityManageUsersBinding

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).userDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageUsersBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.llUserList.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }
    }
}