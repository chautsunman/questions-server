package com.example.questions.service

import com.example.questions.controller.QuestionGroupReqBody
import com.example.questions.data.User
import com.mongodb.TransactionOptions
import com.mongodb.client.TransactionBody
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import org.apache.logging.log4j.kotlin.Logging

class UserServiceImpl(
        private val mongoDbClient: MongoDbClient,
        private val questionGroupsService: QuestionGroupsService
): UserService, Logging {
    override fun setUpUser(user: User): Boolean {
        val uid = user.uid ?: return true

        val clientSession = mongoDbClient.getClient().startSession()

        val txnOptions = TransactionOptions.builder().build()

        val txnBody = TransactionBody<Boolean> {
            val userCollection = mongoDbClient.getDb().getCollection(USERS_COLLECTION, User::class.java)
            val userRes = userCollection.find(eq(UID_FIELD, uid))
            logger.info("new user $uid, record: ${userRes.count()}")

            val hasQuestionGroup = questionGroupsService.getQuestionGroups(uid, null).isNotEmpty()
            if (!hasQuestionGroup) {
                val questionGroup = QuestionGroupReqBody(name = "My Questions")
                questionGroupsService.addQuestionGroup(clientSession, questionGroup, listOf(uid), listOf(uid))
            }

            userCollection.replaceOne(clientSession, eq(UID_FIELD, uid), user, ReplaceOptions().upsert(true))

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

    override fun searchUser(email: String): User? {
        val usersCollection = mongoDbClient.getDb().getCollection(USERS_COLLECTION, User::class.java)

        val res = usersCollection.find(eq("email", email))

        if (res.count() == 0) {
            return null
        }

        return res.first()
    }
}
