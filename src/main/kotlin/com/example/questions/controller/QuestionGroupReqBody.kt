package com.example.questions.controller

data class QuestionGroupReqBody(
        val id: String? = null,
        val name: String = "",
        val users: List<String> = emptyList(),
        val owners: List<String>? = emptyList()
)
