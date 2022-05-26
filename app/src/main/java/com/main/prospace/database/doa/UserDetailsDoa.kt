package com.main.prospace.database.doa

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.main.prospace.database.model.UserDetailsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailsDoa {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserDetails(plants: UserDetailsModel)

    @Query("SELECT * FROM user_details_table")
    fun getUserList(): MutableList<UserDetailsModel>

    @Query("SELECT * FROM user_details_table WHERE email LIKE :email AND password LIKE :password")
    fun loginCheck(email: String, password: String): MutableList<UserDetailsModel>

    @Query("SELECT * FROM user_details_table WHERE email LIKE :email")
    fun getLoggedUser(email: String): UserDetailsModel

}