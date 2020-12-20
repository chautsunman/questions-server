package com.example.questions.service

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase

class MongoDbClient {
    private val DB_NAME = "Questions"

    private var mongoDatabase: MongoDatabase
    private var mongoClient: MongoClient

    init {
        mongoClient = MongoClients.create()
        mongoDatabase = mongoClient.getDatabase(DB_NAME)
    }

    fun getDb(): MongoDatabase = mongoDatabase

    fun getClient(): MongoClient = mongoClient
}