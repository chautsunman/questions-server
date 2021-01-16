package com.example.questions.data

data class QuestionGroup(
        val id: String? = null,
        val name: String,
        val users: List<String> = emptyList(),
        val userDetails: List<User>? = emptyList(),
        val owners: List<String>? = emptyList()
)
