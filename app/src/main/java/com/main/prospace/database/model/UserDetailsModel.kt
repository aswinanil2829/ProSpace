package com.main.prospace.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_details_table", indices = [Index(value = arrayOf("uname"), unique = true)])
data class UserDetailsModel(
    @ColumnInfo(name = "uname")
    val uname: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "characterType")
    val characterType: String,
    @ColumnInfo(name = "password")
    val password: String,
) {
    @PrimaryKey(autoGenerate = true)
    var tableId: Int = 0
}