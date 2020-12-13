package com.example.questions.service

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase

class MongoDbClient {
    private val DB_NAME = "Questions"

    private var mongoDatabase: MongoDatabase

    init {
        val mongoClient = MongoClients.create()
        mongoDatabase = mongoClient.getDatabase(DB_NAME)
    }

    fun getDb(): MongoDatabase {
        return mongoDatabase
    }
}