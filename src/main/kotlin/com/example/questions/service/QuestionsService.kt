package com.example.questions.service

import com.example.questions.Question

interface QuestionsService {
    fun getQuestions(id: String?): List<Question>

    fun addQuestion(question: Question): String?

    fun updateQuestion(question: Question): String?

    fun getRandomQuestion(): Question?
}
