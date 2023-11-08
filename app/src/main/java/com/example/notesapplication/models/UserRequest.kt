package com.example.notesapplication.models

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)