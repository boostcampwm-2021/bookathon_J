package com.tyehooney.fedyourpet.ui

import com.tyehooney.fedyourpet.model.Pet

interface MainListener {
    fun onGetMyPetsSuccess(pets: List<Pet>)
    fun onGetMyPetsFailed(msg: String)
}