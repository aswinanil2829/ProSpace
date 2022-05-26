package com.main.prospace.interfaces

import com.main.prospace.database.model.TaskDetailsModel

interface ITaskClickListenerListener {
    fun onTaskClickListenerListener(
        item: TaskDetailsModel,
        position: Int,
    )
}