package com.example.questions.service

import com.example.questions.controller.QuestionGroupReqBody
import com.example.questions.data.QuestionGroup
import com.mongodb.client.ClientSession

interface QuestionGroupsService {
    fun getQuestionGroups(uid: String, id: String?): List<QuestionGroup>

    fun addQuestionGroup(questionGroup: QuestionGroupReqBody, uid: String): String?

    fun addQuestionGroup(clientSession: ClientSession?, questionGroup: QuestionGroupReqBody, owners: List<String>, users: List<String>): String?

    fun updateQuestionGroup(questionGroup: QuestionGroupReqBody, uid: String): String?

    fun hasUser(questionGroupId: String, uid: String): Boolean
}
