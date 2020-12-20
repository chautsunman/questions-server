package com.example.questions.service

import com.mongodb.TransactionOptions
import com.mongodb.client.TransactionBody
import com.mongodb.client.model.Filters.eq
import org.apache.logging.log4j.kotlin.Logging
import org.bson.BsonArray
import org.bson.BsonObjectId
import org.bson.Document
import org.bson.types.ObjectId

class UserServiceImpl(
        private val mongoDbClient: MongoDbClient
): UserService, Logging {
    private val USERS_COLLECTION = "users"
    private val QUESTION_GROUPS_COLLECTION = "questionGroups"

    override fun setUpUser(uid: String): Boolean {
        val clientSession = mongoDbClient.getClient().startSession()

        val txnOptions = TransactionOptions.builder().build()

        val txnBody = TransactionBody<Boolean> {
            val userCollection = mongoDbClient.getDb().getCollection(USERS_COLLECTION)
            val userRes = userCollection.find(eq("uid", uid))
            logger.info("new user $uid, record: ${userRes.count()}")

            val userDoc: Document
            if (userRes.count() == 0) {
                userDoc = Document("_id", ObjectId())
                with(userDoc) {
                    append("uid", uid)
                }
                userCollection.insertOne(clientSession, userDoc)
            } else {
                userDoc = userRes.first() as Document
            }

            if (!userDoc.containsKey("questionGroups")) {
                val questionGroupsCollection = mongoDbClient.getDb().getCollection(QUESTION_GROUPS_COLLECTION)
                val userQuestionGroupId = ObjectId()
                val userQuestionGroupDoc = Document("_id", userQuestionGroupId)
                with (userQuestionGroupDoc) {
                    append("name", "My Questions")
                }
                questionGroupsCollection.insertOne(clientSession, userQuestionGroupDoc)

                userDoc.append("questionGroups", BsonArray(listOf(BsonObjectId(userQuestionGroupId))))
            }

            userCollection.replaceOne(clientSession, eq("_id", userDoc["_id"]), userDoc)

            return@TransactionBody true
        }

        return try {
            clientSession.withTransaction(txnBody, txnOptions)
            true
        } catch (e: RuntimeException) {
            logger.info("cannot set up user $uid", e)
            false
        } finally {
            clientSession.close()
        }
    }
}
