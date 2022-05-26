package com.main.prospace.interfaces

import com.main.prospace.database.model.TaskDetailsModel
import com.main.prospace.database.model.TestDetailsModel

interface ITestClickListenerListener {
    fun onTestClickListenerListener(
        item: TestDetailsModel,
        position: Int,
    )
}