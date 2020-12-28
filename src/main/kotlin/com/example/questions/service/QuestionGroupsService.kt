package com.example.questions.service

import com.example.questions.data.QuestionGroup

interface QuestionGroupsService {
    fun getQuestionGroups(uid: String): List<QuestionGroup>

    fun addQuestionGroup(questionGroup: QuestionGroup, uid: String): String?

    fun updateQuestionGroup(questionGroup: QuestionGroup, uid: String): String?
}
