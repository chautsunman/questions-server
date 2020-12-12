package com.example.questions

interface QuestionsService {
    fun getQuestions(id: String?): List<Question>

    fun addQuestion(question: Question): String?

    fun updateQuestion(question: Question): String?

    fun getRandomQuestion(): Question?
}
