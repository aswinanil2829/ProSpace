package com.main.prospace.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "task_table", indices = [Index(value = arrayOf("task"), unique = true)])
data class TaskDetailsModel(
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "characterType")
    val characterType: String,
    @ColumnInfo(name = "task")
    val task: String
) {
    @PrimaryKey(autoGenerate = true)
    var tableId: Int = 0
}