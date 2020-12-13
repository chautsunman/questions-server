package com.example.questions

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Aggregates.sample
import com.mongodb.client.model.Filters.eq

class QuestionsServiceImpl(
        private val questionDocumentFactory: QuestionDocumentFactory
) : QuestionsService {
    private val DB_NAME = "Questions"

    private var mongoDatabase: MongoDatabase

    init {
        val mongoClient = MongoClients.create()
        mongoDatabase = mongoClient.getDatabase(DB_NAME)
    }

    override fun getQuestions(id: String?): List<Question> {
        val questionCollection = mongoDatabase.getCollection("questions")

        val res = if (id == null)
            questionCollection.find()
            else questionCollection.find(eq("id", id))
        val questions = res.asIterable().map {
            document -> Question(document.getString("id"), document.getString("question"))
        }

        return questions
    }

    override fun addQuestion(question: Question): String? {
        val doc = questionDocumentFactory.newDoc(question)

        val res = mongoDatabase.getCollection("questions").insertOne(doc)

        return if (res.wasAcknowledged() && doc.contains("id")) doc.getString("id") else null
    }

    override fun updateQuestion(question: Question): String? {
        val doc = questionDocumentFactory.getDoc(question)

        val res = mongoDatabase.getCollection("questions").replaceOne(eq("id", question.id), doc)

        return if (res.modifiedCount >= 1) question.id else null
    }

    override fun getRandomQuestion(): Question? {
        val questionCollection = mongoDatabase.getCollection("questions")

        val randomQuestionDoc = questionCollection.aggregate(listOf(sample(1))).asIterable().firstOrNull()

        val randomQuestion = if (randomQuestionDoc != null)
            Question(randomQuestionDoc.getString("id"), randomQuestionDoc.getString("question"))
            else null

        return randomQuestion
    }
}
