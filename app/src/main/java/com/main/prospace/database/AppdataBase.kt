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

package com.main.prospace.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.main.prospace.database.doa.TaskDetailsDoa
import com.main.prospace.database.doa.TestDetailsDoa
import com.main.prospace.database.doa.UserDetailsDoa
import com.main.prospace.database.model.TaskDetailsModel
import com.main.prospace.database.model.TestDetailsModel
import com.main.prospace.database.model.UserDetailsModel


@Database(
    entities = [UserDetailsModel::class, TaskDetailsModel::class, TestDetailsModel::class],
    version = 4,
    exportSchema = false
)
abstract class AppdataBase : RoomDatabase() {


    abstract fun userDetails(): UserDetailsDoa
    abstract fun testDetails(): TestDetailsDoa
    abstract fun taskDetails(): TaskDetailsDoa

    companion object {
        @Volatile
        private var instance: AppdataBase? = null
        fun getInstance(context: Context): AppdataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppdataBase {
            return Room.databaseBuilder(context, AppdataBase::class.java, "ProSpace")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }

    }


}