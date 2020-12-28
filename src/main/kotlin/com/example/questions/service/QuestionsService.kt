package com.example.questions.service

import com.example.questions.data.Question

interface QuestionsService {
    fun getQuestions(id: String?): List<Question>

    fun addQuestion(groupId: String, question: Question): String?

    fun updateQuestion(groupId: String, question: Question): String?

    fun getRandomQuestion(groupId: String): Question?
}
