package com.example.questions.service

import com.example.questions.data.User

interface UserService {
    fun setUpUser(user: User): Boolean

    fun searchUser(email: String): User?
}
