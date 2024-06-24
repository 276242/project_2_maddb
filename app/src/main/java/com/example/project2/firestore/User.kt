package com.example.project2.firestore

data class User(
    val id: String = "",
    val name: String = "",
    val registeredUser: Boolean = false,
    val email: String = ""
)