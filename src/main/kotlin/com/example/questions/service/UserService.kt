package com.example.questions.service

interface UserService {
    fun createUserIfNotExists(uid: String): Boolean
}
