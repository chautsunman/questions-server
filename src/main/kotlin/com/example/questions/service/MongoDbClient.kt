package com.example.questions.service

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider


class MongoDbClient {
    private val DB_NAME = "Questions"

    private var mongoDatabase: MongoDatabase
    private var mongoClient: MongoClient

    init {
        val pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build())
        val codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)
        val clientSettings = MongoClientSettings.builder()
                .codecRegistry(codecRegistry)
                .build()
        mongoClient = MongoClients.create(clientSettings)
        mongoDatabase = mongoClient.getDatabase(DB_NAME)
    }

    fun getDb(): MongoDatabase = mongoDatabase

    fun getClient(): MongoClient = mongoClient
}