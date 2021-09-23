package com.tyehooney.fedyourpet.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _phoneNum = MutableLiveData<String>()
    val phoneNum: LiveData<String> = _phoneNum
}