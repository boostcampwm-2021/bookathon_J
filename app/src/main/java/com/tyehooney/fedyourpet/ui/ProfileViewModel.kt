package com.tyehooney.fedyourpet.ui

import androidx.lifecycle.ViewModel

// FIXME: Dummy
class Family {
    var id = 1
    var name = "아빠"
}

class ProfileViewModel : ViewModel() {
    val profiles = mutableListOf<Family>()

    init {
        // Dummy
        val a = Family()
        a.name = "아빠"

        val b = Family()
        b.name = "엄마"

        profiles.add(a)
        profiles.add(b)
    }
}