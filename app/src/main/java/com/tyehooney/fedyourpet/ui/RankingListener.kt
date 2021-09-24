package com.tyehooney.fedyourpet.ui

import com.tyehooney.fedyourpet.model.Pet

interface RankingListener {
    fun onProfileReceived(profiles: List<String>)
    fun onGetMyPetsSuccess(pets: List<Pet>)
    fun onGetMyPetsFailed(msg: String)
}
