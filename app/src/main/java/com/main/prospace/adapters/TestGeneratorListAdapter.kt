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
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import com.main.prospace.R
import com.main.prospace.database.model.TestDetailsModel
import com.main.prospace.databinding.ItemTestListBinding
import com.main.prospace.helpers.CommonUtils
import com.main.prospace.interfaces.ITestGeneratorClickListenerListener


class TestGeneratorListAdapter(
    val context: Context,
    private val list: ArrayList<TestDetailsModel>
) :
    RecyclerView.Adapter<TestGeneratorListAdapter.ViewHolder>() {

    lateinit var clickListener: ITestGeneratorClickListenerListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemTestListBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_test_list, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvQuestion.text = item.test
        holder.binding.tvQuestionCount.text =
            ((position + 1).toString() + "/" + list.size.toString())

        when (item.answerInputFormat) {
            CommonUtils.EDIT_TEXT -> {
                holder.binding.etAnswer.visibility = View.VISIBLE
                holder.binding.rbAnswer.visibility = View.GONE
                holder.binding.sbAnswer.visibility = View.GONE
                holder.binding.radioGroup.visibility = View.GONE
            }
            CommonUtils.RATING_BAR -> {
                holder.binding.etAnswer.visibility = View.GONE
                holder.binding.rbAnswer.visibility = View.VISIBLE
                holder.binding.sbAnswer.visibility = View.GONE
                holder.binding.radioGroup.visibility = View.GONE
            }
            CommonUtils.SEEK_BAR -> {
                holder.binding.etAnswer.visibility = View.GONE
                holder.binding.rbAnswer.visibility = View.GONE
                holder.binding.sbAnswer.visibility = View.VISIBLE
                holder.binding.radioGroup.visibility = View.GONE
            }
            CommonUtils.RADIO_BUTTON -> {
                holder.binding.etAnswer.visibility = View.GONE
                holder.binding.rbAnswer.visibility = View.GONE
                holder.binding.sbAnswer.visibility = View.GONE
                holder.binding.radioGroup.visibility = View.VISIBLE
            }
        }
        holder.binding.sbAnswer.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            clickListener.onTestGeneratorClickListenerListener(
                rating = holder.binding.sbAnswer.value,
                position = position
            )
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemTestListBinding) :
        RecyclerView.ViewHolder(binding.root)
}