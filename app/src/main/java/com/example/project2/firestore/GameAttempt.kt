package com.example.project2.firestore

data class GameAttempt(
    val completionTime: Long? = null,
    val correctAnswers: Int? = null,
    val wrongAnswers: Int? = null,
    val totalAnswers: Int? = null,
    val totalAttempts: Int? = null,
    val bestScore: Long? = null
)