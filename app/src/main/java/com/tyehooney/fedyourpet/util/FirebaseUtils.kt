package com.tyehooney.fedyourpet.util

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.tyehooney.fedyourpet.ui.LoginListener
import java.util.concurrent.TimeUnit

fun sendVerifyingCode(
    phone: String,
    activity: Activity,
    loginListener: LoginListener
) {
    FirebaseAuth.getInstance().firebaseAuthSettings
        .setAppVerificationDisabledForTesting(true)

    val options = PhoneAuthOptions.newBuilder()
        .setPhoneNumber(phone)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0, loginListener)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                p0.message?.let {
                    loginListener.onLoginFailed(it)
                }
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                loginListener.onVerifyingCodeReceived(p0)
            }
        })
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}

fun signInWithVerifyingCode(
    verificationId: String,
    verifyingCode: String,
    loginListener: LoginListener
) {
    val credential = PhoneAuthProvider.getCredential(verificationId, verifyingCode)
    signInWithPhoneAuthCredential(credential, loginListener)
}

private fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    loginListener: LoginListener
) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithCredential(credential)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.user?.let { user ->
                    loginListener.onLoginSuccess(user.uid)
                }
            } else {
                it.exception?.message?.let { msg ->
                    loginListener.onLoginFailed(msg)
                }
            }
        }
}

private fun addNewUser(uid: String) {

}