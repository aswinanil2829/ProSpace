package com.main.prospace.database.doa

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.main.prospace.database.model.TaskDetailsModel
import com.main.prospace.database.model.TestDetailsModel
import com.main.prospace.database.model.UserDetailsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDetailsDoa {
    @Query("SELECT * FROM test_table")
    fun getTestList(): MutableList<TestDetailsModel>

    @Query("SELECT * FROM test_table WHERE characterType LIKE :characterType")
    fun getTestListWithCharacter(characterType: String): MutableList<TestDetailsModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTestDetails(plants: TestDetailsModel)

    @Query("SELECT * FROM test_table WHERE tableId LIKE :id")
    fun getTestDetails(id: Int): TestDetailsModel

    @Query("UPDATE test_table SET date = :date ,characterType= :characterType,test= :task, answerInputFormat= :answerInputFormat WHERE tableId LIKE :id")
    fun updateTestDetails(id: Int, date: String, characterType: String, task: String, answerInputFormat: String)

    @Query("DELETE FROM test_table WHERE tableId LIKE :id ")
    fun deleteTestDetails(id: Int)

}