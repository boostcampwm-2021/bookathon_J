package com.tyehooney.fedyourpet.util

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tyehooney.fedyourpet.model.User
import com.tyehooney.fedyourpet.ui.LoginListener
import com.tyehooney.fedyourpet.ui.ProfileListener
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

fun addNewUser(uid: String, phone: String) {
    val usersCollection = Firebase.firestore.collection("Users")
    usersCollection.whereEqualTo("id", uid).get().addOnSuccessListener {
        if (it.isEmpty) {
            val user = User(uid, phone, listOf("나"))
            usersCollection.document(uid).set(user)
                .addOnSuccessListener { Log.d("Login", "add user: success") }
                .addOnFailureListener { e -> Log.e("Login", "add user: failed ${e.message}") }
        }
    }
}

fun getProfiles(uid: String, profileListener: ProfileListener) {
    val usersCollection = Firebase.firestore.collection("Users")
    usersCollection.document(uid).get()
        .addOnSuccessListener {
            it.toObject(User::class.java)?.profiles?.let { profiles ->
                profileListener.onProfileReceived(profiles)
            }
        }
}

fun addNewProfile(uid: String, newProfiles: List<String>, profileListener: ProfileListener) {
    val usersCollection = Firebase.firestore.collection("Users")
    usersCollection.document(uid).update("profiles", newProfiles)
        .addOnSuccessListener {
            profileListener.onNewProfileAdded()
        }
}