package com.example.questions

interface QuestionsService {
    fun getQuestions(): List<Question>

    fun addQuestion(question: Question): Boolean
}
