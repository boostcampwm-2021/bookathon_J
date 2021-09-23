package com.tyehooney.fedyourpet.model

data class Pet(
    val ownerId: String = "",
    val name: String = "",
    val image: String = "",
    val feedingTimes: List<String> = emptyList()
)
