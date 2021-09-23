package com.tyehooney.fedyourpet.ui

interface AnimalAddListener {
    fun onNewPetAdded()
    fun onAddNewPetFailed(msg: String)
}