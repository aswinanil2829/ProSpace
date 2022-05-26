package com.main.prospace.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "test_table", indices = [Index(value = arrayOf("test"), unique = true)])
data class TestDetailsModel(
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "characterType")
    val characterType: String,
    @ColumnInfo(name = "test")
    val test: String,
    @ColumnInfo(name = "answerInputFormat")
    val answerInputFormat: String,
) {
    @PrimaryKey(autoGenerate = true)
    var tableId: Int = 0
}