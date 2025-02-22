package com.example.project2.firestore

import android.content.ContentValues.TAG
import android.util.Log
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

//    fun saveMemoryGameAttemptFS(userId: String, attempt: GameAttempt) {
//        val gameRef = firestore.collection("users")
//            .document(userId)
//            .collection("games")
//            .document("memoryGame")
//
//        gameRef.collection("attempts")
//            .add(attempt)
//            .addOnSuccessListener {
//                // Successfully saved attempt
//            }
//            .addOnFailureListener {
//                // Handle failure to save attempt
//            }
//
//        // Update bestScore and totalAttempts
//        firestore.runTransaction { transaction ->
//            val snapshot = transaction.get(gameRef)
//
//            val currentBestScore = snapshot.getLong("bestScore") ?: Long.MAX_VALUE
//            val currentTotalAttempts = snapshot.getLong("totalAttempts") ?: 0
//
//            if (attempt.completionTime!! < currentBestScore) {
//                transaction.update(gameRef, "bestScore", attempt.completionTime)
//            }
//
//            transaction.update(gameRef, "totalAttempts", currentTotalAttempts + 1)
//        }.addOnSuccessListener {
//            // Successfully updated game statistics
//        }.addOnFailureListener { e ->
//            // Handle failure to update game statistics
//        }
//    }

    fun saveMemoryGameAttemptFS(userId: String, attempt: GameAttempt) {
        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .collection("games")
            .document("memoryGame")
            .collection("attempts")
            .add(attempt)
            .addOnSuccessListener {
                // Successfully saved attempt
            }
            .addOnFailureListener {
                // Handle failure to save attempt
            }
    }

    fun updateGameStatsFS(userId: String, gameType: String, bestScore: Int, totalAttempts: Int) {
        val firestore = FirebaseFirestore.getInstance()
        val gameRef = firestore.collection("users")
            .document(userId)
            .collection("games")
            .document(gameType)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(gameRef)

            if (!snapshot.exists()) {
                // Initialize the game stats document if it doesn't exist
                val gameStats = hashMapOf(
                    "bestScore" to bestScore,
                    "totalAttempts" to totalAttempts
                )
                transaction.set(gameRef, gameStats)
            } else {
                val currentBestScore = snapshot.getLong("bestScore")?.toInt() ?: -1
                val currentTotalAttempts = snapshot.getLong("totalAttempts")?.toInt() ?: 0

                // Update the best score if the new score is better or if the current best score is -1
                if (currentBestScore == -1 || bestScore > currentBestScore) {
                    transaction.update(gameRef, "bestScore", bestScore)
                }

                // Increment the total attempts
                transaction.update(gameRef, "totalAttempts", currentTotalAttempts + 1)
            }
        }.addOnSuccessListener {
            // Successfully updated game statistics
        }.addOnFailureListener { e ->
            // Handle failure to update game statistics
        }
    }




    fun getGameStatsFS(userId: String, gameType: String, callback: (GameStats?) -> Unit) {
        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .collection("games")
            .document(gameType)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val bestScore = document.getLong("bestScore")?.toInt() ?: 0
                    val totalAttempts = document.getLong("totalAttempts")?.toInt() ?: 0
                    // Add other game stats fields if needed
                    callback(GameStats(bestScore, totalAttempts))
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                // Handle failure if needed
                callback(null)
            }
    }

    fun saveStroopGameAttemptFS(userId: String, attempt: GameAttempt) {
        firestore.collection("users")
            .document(userId)
            .collection("games")
            .document("stroopTestGame")
            .collection("attempts")
            .add(attempt)
            .addOnSuccessListener {
                // Successfully saved attempt
            }
            .addOnFailureListener {
                // Handle failure to save attempt
            }
    }

    fun saveVisualSearchGameAttemptFS(userId: String, attempt: GameAttempt) {
        firestore.collection("users")
            .document(userId)
            .collection("games")
            .document("visualSearchTestGame")
            .collection("attempts")
            .add(attempt)
            .addOnSuccessListener {
                // Successfully saved attempt
            }
            .addOnFailureListener {
                // Handle failure to save attempt
            }
    }


//    private fun saveGameAttemptFS(userId: String, gameType: String, attempt: GameAttempt) {
//        FirebaseFirestore.getInstance().collection("users")
//            .document(userId)
//            .collection("games")
//            .document(gameType)
//            .collection("attempts")
//            .add(attempt)
//            .addOnSuccessListener { }
//            .addOnFailureListener { }
//    }
 }