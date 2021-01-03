package com.example.questions.service

import com.example.questions.data.QuestionGroup
import com.example.questions.service.data.QuestionGroupMapper
import com.example.questions.service.data.QuestionGroupObj
import com.mongodb.client.ClientSession
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import org.apache.logging.log4j.kotlin.Logging
import org.bson.conversions.Bson
import org.bson.types.ObjectId

class QuestionGroupsServiceImpl(
        private val mongoDbClient: MongoDbClient,
        private val questionGroupMapper: QuestionGroupMapper
) : QuestionGroupsService, Logging {
    override fun getQuestionGroups(uid: String, id: String?): List<QuestionGroup> {
        val questionGroupsCollection = mongoDbClient.getDb()
                .getCollection(QUESTION_GROUPS_COLLECTION, QuestionGroupObj::class.java)

        val filters: ArrayList<Bson> = ArrayList()
        filters.add(eq("users", uid))
        if (id != null) {
            filters.add(eq(OBJECT_ID_FIELD, ObjectId(id)))
        }

        val res = if (filters.isNotEmpty())
                questionGroupsCollection.find(Filters.and(filters))
                else questionGroupsCollection.find()
        val questionGroups = res.map { questionGroupObj -> questionGroupMapper.decode(questionGroupObj) }.toList()

        return questionGroups
    }

    override fun addQuestionGroup(questionGroup: QuestionGroup, uid: String): String? {
        return addQuestionGroup(null, questionGroup, uid)
    }

    override fun addQuestionGroup(clientSession: ClientSession?, questionGroup: QuestionGroup, uid: String): String? {
        val questionGroupObj = questionGroupMapper.encode(questionGroup)
        questionGroupObj.users = listOf(uid)

        val questionGroupsCollection = mongoDbClient.getDb()
                .getCollection(QUESTION_GROUPS_COLLECTION, QuestionGroupObj::class.java)
        val res = if (clientSession == null)
                questionGroupsCollection.insertOne(questionGroupObj)
                else questionGroupsCollection.insertOne(clientSession, questionGroupObj)

        return if (res.wasAcknowledged()) questionGroupObj.id?.toHexString() else null
    }

    override fun updateQuestionGroup(questionGroup: QuestionGroup, uid: String): String? {
        val oldQuestionGroups = getQuestionGroups(uid, questionGroup.id)
        if (oldQuestionGroups.isEmpty()) {
            return null
        }
        val oldQuestionGroup = oldQuestionGroups[0]

        val _questionGroup = oldQuestionGroup.copy(name = questionGroup.name)
        val questionGroupObj = questionGroupMapper.encode(_questionGroup)

        val res = mongoDbClient.getDb()
                .getCollection(QUESTION_GROUPS_COLLECTION, QuestionGroupObj::class.java)
                .replaceOne(eq(OBJECT_ID_FIELD, questionGroupObj.id), questionGroupObj)

        return if (res.modifiedCount >= 1) questionGroup.id else null
    }
}
