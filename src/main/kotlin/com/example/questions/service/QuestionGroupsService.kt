package com.example.questions.service

import com.example.questions.data.QuestionGroup
import com.mongodb.client.ClientSession

interface QuestionGroupsService {
    fun getQuestionGroups(uid: String, id: String?): List<QuestionGroup>

    fun addQuestionGroup(questionGroup: QuestionGroup, uid: String): String?

    fun addQuestionGroup(clientSession: ClientSession?, questionGroup: QuestionGroup, uid: String): String?

    fun updateQuestionGroup(questionGroup: QuestionGroup, uid: String): String?
}
