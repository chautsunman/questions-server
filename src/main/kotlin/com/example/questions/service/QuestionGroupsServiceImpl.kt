package com.example.questions.service

import com.example.questions.data.QuestionGroup
import com.example.questions.data.User
import com.mongodb.client.ClientSession
import com.mongodb.client.model.Aggregates.lookup
import com.mongodb.client.model.Aggregates.match
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.set
import org.apache.logging.log4j.kotlin.Logging
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId

class QuestionGroupsServiceImpl(
        private val mongoDbClient: MongoDbClient
) : QuestionGroupsService, Logging {
    override fun getQuestionGroups(uid: String, id: String?): List<QuestionGroup> {
        val questionGroupsCollection = mongoDbClient.getDb().getCollection(QUESTION_GROUPS_COLLECTION)

        val filters: ArrayList<Bson> = ArrayList()
        filters.add(eq("users", uid))
        if (id != null) {
            filters.add(eq(OBJECT_ID_FIELD, ObjectId(id)))
        }

        val aggregateList: ArrayList<Bson> = ArrayList()
        aggregateList.add(match(Filters.and(filters)))
        aggregateList.add(lookup(USERS_COLLECTION, "users", "uid", "users_details"))

        val res = questionGroupsCollection.aggregate(aggregateList)

        return res.map { questionGroupDoc ->
            val id = questionGroupDoc.getObjectId("_id")
            val name = questionGroupDoc["name", ""]

            val users = questionGroupDoc.getList("users", String::class.java) as List<String>
            val userDetailsDocs = questionGroupDoc.getList("users_details", Document::class.java, emptyList())
            val userDetails: ArrayList<User> = ArrayList()
            for (userDetailsDoc in userDetailsDocs) {
                val user = User(
                        userDetailsDoc.getOrDefault("uid", null) as String?,
                        userDetailsDoc.getOrDefault("email", null) as String?)
                if (user.uid != null) {
                    userDetails.add(user)
                }
            }

            val owners = questionGroupDoc.getList("owners", String::class.java) as List<String>

            QuestionGroup(
                    id.toHexString(),
                    name,
                    users = users,
                    userDetails = userDetails,
                    owners = owners
            )
        }.toList()
    }

    override fun addQuestionGroup(questionGroup: QuestionGroup, uid: String): String? {
        return addQuestionGroup(null, questionGroup, uid)
    }

    override fun addQuestionGroup(clientSession: ClientSession?, questionGroup: QuestionGroup, uid: String): String? {
        val objectId = ObjectId()
        val questionGroupDoc = Document("_id", objectId)
        questionGroupDoc["name"] = questionGroup.name
        questionGroupDoc["users"] = listOf(uid)
        questionGroupDoc["owners"] = listOf(uid)

        val questionGroupsCollection = mongoDbClient.getDb()
                .getCollection(QUESTION_GROUPS_COLLECTION)
        val res = if (clientSession == null)
                questionGroupsCollection.insertOne(questionGroupDoc)
                else questionGroupsCollection.insertOne(clientSession, questionGroupDoc)

        return if (res.wasAcknowledged()) objectId.toHexString() else null
    }

    override fun updateQuestionGroup(questionGroup: QuestionGroup, uid: String): String? {
        val res = mongoDbClient.getDb()
                .getCollection(QUESTION_GROUPS_COLLECTION)
                .updateOne(eq(OBJECT_ID_FIELD, ObjectId(questionGroup.id)), listOf(
                        set("name", questionGroup.name),
                        set("users", questionGroup.users)
                ))

        return if (res.modifiedCount >= 1) questionGroup.id else null
    }

    override fun hasUser(questionGroupId: String, uid: String): Boolean {
        return mongoDbClient.getDb().getCollection(QUESTION_GROUPS_COLLECTION)
                .find(Filters.and(listOf(
                        eq(OBJECT_ID_FIELD, ObjectId(questionGroupId)),
                        eq("users", uid))
                )).count() == 1
    }
}
