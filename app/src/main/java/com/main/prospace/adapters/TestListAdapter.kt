/*
 * Copyright (c) 2021 Estrrado. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.main.prospace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.main.prospace.R
import com.main.prospace.database.model.TaskDetailsModel
import com.main.prospace.database.model.TestDetailsModel
import com.main.prospace.database.model.UserDetailsModel
import com.main.prospace.databinding.ItemUserListBinding
import com.main.prospace.interfaces.ITaskClickListenerListener
import com.main.prospace.interfaces.ITestClickListenerListener

class TestListAdapter(
    val context: Context,
    private val list: ArrayList<TestDetailsModel>
) :
    RecyclerView.Adapter<TestListAdapter.ViewHolder>() {

    lateinit var clickListener: ITestClickListenerListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemUserListBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_user_list, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvName.text = item.test

        holder.binding.root.setOnClickListener {
            clickListener.onTestClickListenerListener(item = item, position = position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root)
}