package com.tyehooney.fedyourpet.model

import java.util.*

data class FeedLog(
    val petId: String = "",
    val ownerId: String = "",
    val who: String = "",
    val feedAt: Date = Date()
)