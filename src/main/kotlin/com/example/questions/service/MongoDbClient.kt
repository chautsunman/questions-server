package com.example.questions.service

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.apache.logging.log4j.kotlin.Logging
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import javax.annotation.PostConstruct


class MongoDbClient: Logging {
    private val DB_NAME = "Questions"

    private lateinit var mongoDatabase: MongoDatabase
    private lateinit var mongoClient: MongoClient

    @PostConstruct
    fun init() {
        val dbConnection = System.getenv("DB_CONNECTION")
        if (dbConnection == null) {
            logger.error("dbConnection is null")
            throw Exception("dbConnection is null")
        }

        val pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build())
        val codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)
        val clientSettings = MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(dbConnection))
                .codecRegistry(codecRegistry)
                .build()
        mongoClient = MongoClients.create(clientSettings)
        mongoDatabase = mongoClient.getDatabase(DB_NAME)

        logger.info("initialized DB connection")
    }

    fun getDb(): MongoDatabase = mongoDatabase

    fun getClient(): MongoClient = mongoClient
}