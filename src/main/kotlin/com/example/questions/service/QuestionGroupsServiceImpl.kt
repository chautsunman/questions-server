package com.example.questions.service

import com.example.questions.data.QuestionGroup
import com.example.questions.service.data.QuestionGroupMapper
import com.example.questions.service.data.QuestionGroupObj
import com.mongodb.client.model.Aggregates.lookup
import com.mongodb.client.model.Aggregates.match
import com.mongodb.client.model.Filters.eq
import org.apache.logging.log4j.kotlin.Logging
import org.bson.Document
import org.bson.codecs.pojo.annotations.BsonProperty
import java.lang.RuntimeException
import kotlin.collections.ArrayList

class QuestionGroupsServiceImpl(
        private val mongoDbClient: MongoDbClient,
        private val questionGroupMapper: QuestionGroupMapper
) : QuestionGroupsService, Logging {
    data class UserQuestionGroupsRes(
            var userQuestionGroups: List<QuestionGroupObj> = emptyList()
    )

    override fun getQuestionGroups(uid: String): List<QuestionGroup> {
        val usersCollection = mongoDbClient.getDb().getCollection(USERS_COLLECTION)

        val res = usersCollection.aggregate(
                listOf(
                        match(eq(UID_FIELD, uid)),
                        lookup(QUESTION_GROUPS_COLLECTION, "questionGroups", OBJECT_ID_FIELD, "userQuestionGroups")
                ),
                UserQuestionGroupsRes::class.java
        )

        return try {
            if (res.count() != 1) {
                return emptyList()
            }
            val resObj = res.first() ?: return emptyList()

            val questionGroups = resObj.userQuestionGroups.map { questionGroupObj -> questionGroupMapper.decode(questionGroupObj) }

            questionGroups
        } catch (e: RuntimeException) {
            logger.info("get question groups error, uid: $uid", e)
            emptyList()
        }
    }

    override fun addQuestionGroup(questionGroup: QuestionGroup, uid: String): String? {
        val questionGroupObj = questionGroupMapper.encode(questionGroup)

        val res = mongoDbClient.getDb()
                .getCollection(QUESTION_GROUPS_COLLECTION, QuestionGroupObj::class.java)
                .insertOne(questionGroupObj)

        return if (res.wasAcknowledged()) questionGroupObj.id?.toHexString() else null
    }

    override fun updateQuestionGroup(questionGroup: QuestionGroup, uid: String): String? {
        val questionGroupObj = questionGroupMapper.encode(questionGroup)

        val res = mongoDbClient.getDb()
                .getCollection(QUESTION_GROUPS_COLLECTION, QuestionGroupObj::class.java)
                .replaceOne(eq(OBJECT_ID_FIELD, questionGroupObj.id), questionGroupObj)

        return if (res.modifiedCount >= 1) questionGroup.id else null
    }
}
