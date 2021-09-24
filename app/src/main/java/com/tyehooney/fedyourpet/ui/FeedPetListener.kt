package com.tyehooney.fedyourpet.ui

import com.tyehooney.fedyourpet.model.FeedLog

interface FeedPetListener {
    fun onGetLogsSuccess(logs: List<FeedLog>)
    fun onGetLogsFailed(msg: String)
    fun onAddLogSuccess()
    fun onAddLogFailed(msg: String)
}