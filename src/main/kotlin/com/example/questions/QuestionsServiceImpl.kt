package com.example.questions

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase

class QuestionsServiceImpl(
        private val questionDocumentFactory: QuestionDocumentFactory
) : QuestionsService {
    private val DB_NAME = "Questions"

    private var mongoDatabase: MongoDatabase

    init {
        val mongoClient = MongoClients.create()
        mongoDatabase = mongoClient.getDatabase(DB_NAME)
    }

    override fun getQuestions(): List<Question> {
        val questionCollection = mongoDatabase.getCollection("questions")

        val questions = questionCollection.find().asIterable().map {
            document -> Question(document.getString("key"), document.getString("question"))
        }

        return questions
    }

    override fun addQuestion(question: Question): Boolean {
        val doc = questionDocumentFactory.getDoc(question)

        val res = mongoDatabase.getCollection("questions").insertOne(doc)

        return res.wasAcknowledged()
    }
}
