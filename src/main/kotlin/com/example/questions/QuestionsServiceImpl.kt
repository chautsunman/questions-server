package com.example.questions

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase

class QuestionsServiceImpl : QuestionsService {
    private val DB_NAME = "Questions"

    private var mongoDatabase: MongoDatabase

    constructor() {
        var mongoClient = MongoClients.create()
        mongoDatabase = mongoClient.getDatabase(DB_NAME)
    }

    override fun getQuestions(): List<Question> {
        var questionCollection = mongoDatabase.getCollection("questions")

        var questions = questionCollection.find().asIterable().map {
            document -> Question(document.getString("key"), document.getString("question"))
        }

        return questions
    }
}
