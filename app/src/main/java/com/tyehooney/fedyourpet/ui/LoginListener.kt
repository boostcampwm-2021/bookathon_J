package com.tyehooney.fedyourpet.ui

interface LoginListener {
    fun onLoginSuccess(uid: String)
    fun onVerifyingCodeReceived(verificationId: String)
    fun onLoginFailed(msg: String)
}