package com.main.prospace.database.doa

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.main.prospace.database.model.TaskDetailsModel

@Dao
interface TaskDetailsDoa {
    @Query("SELECT * FROM task_table")
    fun getTaskList(): MutableList<TaskDetailsModel>

    @Query("SELECT * FROM task_table WHERE characterType LIKE :characterType")
    fun getTaskListWithCharacter(characterType: String): MutableList<TaskDetailsModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTaskDetails(plants: TaskDetailsModel)

    @Query("SELECT * FROM task_table WHERE tableId LIKE :id")
    fun getTaskDetails(id: Int): TaskDetailsModel

    @Query("UPDATE task_table SET date = :date ,characterType= :characterType,task= :task WHERE tableId LIKE :id")
    fun updateTaskDetails(id: Int, date: String, characterType: String, task: String)

    @Query("DELETE FROM task_table WHERE tableId LIKE :id ")
    fun deleteTaskDetails(id: Int)
}