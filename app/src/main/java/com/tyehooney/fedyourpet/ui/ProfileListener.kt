package com.tyehooney.fedyourpet.ui

interface ProfileListener {
    fun onProfileReceived(profiles: List<String>)
    fun onNewProfileAdded()
}