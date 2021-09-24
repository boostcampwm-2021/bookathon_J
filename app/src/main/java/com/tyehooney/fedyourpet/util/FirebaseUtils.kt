package com.tyehooney.fedyourpet.util

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tyehooney.fedyourpet.model.FeedLog
import com.tyehooney.fedyourpet.model.Pet
import com.tyehooney.fedyourpet.model.User
import com.tyehooney.fedyourpet.ui.*
import java.util.*
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

fun getProfiles(uid: String, rankingListener: RankingListener) {
    val usersCollection = Firebase.firestore.collection("Users")
    usersCollection.document(uid).get()
        .addOnSuccessListener {
            it.toObject(User::class.java)?.profiles?.let { profiles ->
                rankingListener.onProfileReceived(profiles)
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

fun addNewPet(
    uid: String,
    name: String,
    feedingTimes: List<String>,
    profileBitmap: Bitmap,
    animalAddListener: AnimalAddListener
) {

    val data = bitmapToByteArray(profileBitmap)
    val storageRef = Firebase.storage.reference.child("${name}_${System.currentTimeMillis()}.png")
    storageRef.putBytes(data)
        .addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                val petsCollection = Firebase.firestore.collection("Pets")
                val petId = petsCollection.document().id
                petsCollection.document(petId)
                    .set(Pet(petId, uid, name, it.toString(), feedingTimes))
                    .addOnSuccessListener {
                        animalAddListener.onNewPetAdded()
                    }.addOnFailureListener { e ->
                        e.message?.let { msg ->
                            animalAddListener.onAddNewPetFailed(msg)
                        }
                    }
            }
        }
}

fun getMyPets(uid: String, mainListener: MainListener) {
    val petsCollection = Firebase.firestore.collection("Pets")
    petsCollection.whereEqualTo("ownerId", uid).get()
        .addOnSuccessListener {
            mainListener.onGetMyPetsSuccess(it.toObjects(Pet::class.java))
        }.addOnFailureListener {
            it.message?.let { msg ->
                mainListener.onGetMyPetsFailed(msg)
            }
        }
}

fun getTodaysLog(petId: String, feedPetListener: FeedPetListener) {
    val logsCollection = Firebase.firestore.collection("Logs")
    val today = Calendar.getInstance()
    today[Calendar.HOUR] = 0
    today[Calendar.MINUTE] = 0
    logsCollection.whereEqualTo("petId", petId)
        .whereGreaterThan("feedAt", today.time).get()
        .addOnSuccessListener { feedPetListener.onGetLogsSuccess(it.toObjects(FeedLog::class.java)) }
        .addOnFailureListener { e ->
            e.message?.let { feedPetListener.onGetLogsFailed(it) }
        }
}

fun getAllLog(petName: String, petId: String, rankingListener: RankingListener) {
    val logsCollection = Firebase.firestore.collection("Logs")
    logsCollection.whereEqualTo("petId", petId).get()
        .addOnSuccessListener { rankingListener.onGetLogsSuccess(it.toObjects(FeedLog::class.java), petName) }
        .addOnFailureListener { e ->
            e.message?.let { rankingListener.onGetLogsFailed(it) }
        }
}

fun addFeedLog(petId: String, uid: String, profile: String, feedPetListener: FeedPetListener) {
    val logsCollection = Firebase.firestore.collection("Logs")
    val newFeedLog = FeedLog(petId, uid, profile)
    logsCollection.add(newFeedLog)
        .addOnSuccessListener { feedPetListener.onAddLogSuccess() }
        .addOnFailureListener { e ->
            e.message?.let { feedPetListener.onAddLogFailed(it) }
        }
}

fun getMyPets(uid: String, rankingListener: RankingListener) {
    val petsCollection = Firebase.firestore.collection("Pets")
    petsCollection.whereEqualTo("ownerId", uid).get()
        .addOnSuccessListener {
            rankingListener.onGetMyPetsSuccess(it.toObjects(Pet::class.java))
        }.addOnFailureListener {
            it.message?.let { msg ->
                rankingListener.onGetMyPetsFailed(msg)
            }
        }
}