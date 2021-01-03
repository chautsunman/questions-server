package com.example.questions.service

import com.example.questions.data.QuestionGroup
import com.mongodb.TransactionOptions
import com.mongodb.client.TransactionBody
import com.mongodb.client.model.Filters.eq
import org.apache.logging.log4j.kotlin.Logging
import org.bson.Document
import org.bson.types.ObjectId

class UserServiceImpl(
        private val mongoDbClient: MongoDbClient,
        private val questionGroupsService: QuestionGroupsService
): UserService, Logging {
    override fun setUpUser(uid: String): Boolean {
        val clientSession = mongoDbClient.getClient().startSession()

        val txnOptions = TransactionOptions.builder().build()

        val txnBody = TransactionBody<Boolean> {
            val userCollection = mongoDbClient.getDb().getCollection(USERS_COLLECTION)
            val userRes = userCollection.find(eq(UID_FIELD, uid))
            logger.info("new user $uid, record: ${userRes.count()}")

            val userDoc: Document
            var userObjectId: ObjectId
            if (userRes.count() == 0) {
                userObjectId = ObjectId()
                userDoc = Document(OBJECT_ID_FIELD, userObjectId)
                with(userDoc) {
                    append(UID_FIELD, uid)
                }
                userCollection.insertOne(clientSession, userDoc)
            } else {
                userDoc = userRes.first() as Document
                userObjectId = userDoc.getObjectId(OBJECT_ID_FIELD)
            }

            val hasQuestionGroup = questionGroupsService.getQuestionGroups(uid, null).isNotEmpty()
            if (!hasQuestionGroup) {
                val questionGroup = QuestionGroup(name = "My Questions")
                questionGroupsService.addQuestionGroup(clientSession, questionGroup, uid)
            }

            userCollection.replaceOne(clientSession, eq(OBJECT_ID_FIELD, userObjectId), userDoc)

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
