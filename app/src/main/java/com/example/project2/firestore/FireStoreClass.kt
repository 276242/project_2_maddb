package com.example.project2.firestore

import com.example.project2.MainActivity
import com.example.project2.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val firestore = FirebaseFirestore.getInstance()


    fun registerUserFS(activity: RegisterActivity, user: User) {
        firestore.collection("users")
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    fun getUserDetails(activity: MainActivity) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserId = currentUser.uid

            FirebaseFirestore.getInstance().collection("users")
                .document(currentUserId)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        activity.onUserDetailsReceived(user)
                    } else {
                        // Handle case where document does not exist or cannot be mapped to User object
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle Firestore query failure
                }
        } else {
            // Handle case where no user is logged in
        }
    }

    fun saveMemoryGameAttemptFS(userId: String, attempt: GameAttempt) {
        saveGameAttemptFS(userId, "memoryGame", attempt)
    }

    fun saveStroopGameAttemptFS(userId: String, attempt: GameAttempt) {
        saveGameAttemptFS(userId, "stroopTestGame", attempt)
    }

    fun saveVisualSearchGameAttemptFS(userId: String, attempt: GameAttempt) {
        saveGameAttemptFS(userId, "visualSearchTestGame", attempt)
    }

    private fun saveGameAttemptFS(userId: String, gameType: String, attempt: GameAttempt) {
        firestore.collection("users")
            .document(userId)
            .collection("games")
            .document(gameType)
            .collection("attempts")
            .add(attempt)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }



//    fun saveGameAttemptFS(userId: String, gameType: String, attempt: GameAttempt, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
//        firestore.collection("users")
//            .document(userId)
//            .collection("games")
//            .document(gameType)
//            .collection("attempts")
//            .add(attempt)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { e -> onFailure(e.message ?: "Unknown error") }
//    }

    // Example method to update user profile (not necessarily used in this specific context but useful for extending functionality)
//    fun updateUserProfileFS(userId: String, name: String, email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
//        val userUpdates = hashMapOf(
//            "name" to name,
//            "email" to email
//        )
//
//        firestore.collection("users")
//            .document(userId)
//            .update(userUpdates)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { e -> onFailure(e.message ?: "Unknown error") }
//    }
}

//
//class FireStoreClass {
//
//    private val mFireStore = FirebaseFirestore.getInstance()
//
//
//    fun registerUserFS(activity: RegisterActivity, userInfo: User){
//
//        mFireStore.collection("users")
//            .document(userInfo.id)
//            .set(userInfo, SetOptions.merge())
//            .addOnSuccessListener {
//                activity.userRegistrationSuccess()
//
//            }
//            .addOnFailureListener{
//
//            }
//    }