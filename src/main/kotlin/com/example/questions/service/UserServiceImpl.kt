package com.example.questions.service

import com.mongodb.client.model.Filters.eq
import org.apache.logging.log4j.kotlin.Logging
import org.bson.Document
import org.bson.types.ObjectId

class UserServiceImpl(
        private val mongoDbClient: MongoDbClient
): UserService, Logging {
    override fun createUserIfNotExists(uid: String): Boolean {
        val userCollection = mongoDbClient.getDb().getCollection("users")
        val getRes = userCollection.find(eq("uid", uid))
        if (getRes.count() > 0) {
            return true
        }

        logger.info("new user ${uid}")
        val doc = Document("_id", ObjectId())
        with (doc) {
            append("uid", uid)
        }
        val createRes = userCollection.insertOne(doc)
        return createRes.wasAcknowledged()
    }
}
