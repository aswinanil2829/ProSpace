package com.main.prospace.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.main.prospace.R
import com.main.prospace.adapters.UserListAdapter
import com.main.prospace.database.AppdataBase
import com.main.prospace.database.model.UserDetailsModel
import com.main.prospace.databinding.ActivityAdminHomeBinding
import com.main.prospace.databinding.ActivityUserListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

class UserListActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserListBinding

    private val appdataBase by lazy {
        AppdataBase.getInstance(this).userDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        setUserList()
    }

    private fun setUserList() {
        setMyDownloadNotesAdapter(appdataBase.getUserList() as ArrayList<UserDetailsModel>)
    }

    private fun setMyDownloadNotesAdapter(list: ArrayList<UserDetailsModel>) {
        if (list.isNotEmpty()) {
            val adapter = UserListAdapter(this, list)
            val layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
            binding.rvMyDownloadNotes.adapter = adapter
            binding.rvMyDownloadNotes.layoutManager = layoutManager
        } else {
            binding.tvNoDataFound.visibility = View.VISIBLE
        }
    }
}