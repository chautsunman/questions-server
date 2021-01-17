package com.example.questions.service

import com.example.questions.data.Question

interface QuestionsService {
    fun getQuestions(uid: String, groupId: String, id: String?): List<Question>

    fun addQuestion(uid: String, groupId: String, question: Question): String?

    fun updateQuestion(uid: String, groupId: String, question: Question): String?

    fun getRandomQuestion(uid: String, groupId: String): Question?
}
