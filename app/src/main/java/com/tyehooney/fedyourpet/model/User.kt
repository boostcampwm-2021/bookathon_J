package com.tyehooney.fedyourpet.model

data class User(
    val id: String = "",
    val phone: String = "",
    val profiles: List<String> = emptyList()
    )
