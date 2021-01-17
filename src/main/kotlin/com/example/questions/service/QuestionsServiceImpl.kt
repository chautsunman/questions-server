package com.example.questions.service

import com.example.questions.data.Question
import com.mongodb.client.model.Aggregates.match
import com.mongodb.client.model.Aggregates.sample
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.set
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId

class QuestionsServiceImpl(
        private val mongoDbClient: MongoDbClient
) : QuestionsService {
    override fun getQuestions(uid: String, groupId: String?, id: String?): List<Question> {
        val questionCollection = mongoDbClient.getDb().getCollection(QUESTIONS_COLLECTION)

        val filters: ArrayList<Bson> = ArrayList()
        if (groupId != null) {
            filters.add(eq("questionGroupId", ObjectId(groupId)))
        }
        if (id != null) {
            filters.add(eq(OBJECT_ID_FIELD, ObjectId(id)))
        }

        val res = if (filters.isNotEmpty())
            questionCollection.aggregate(listOf(match(and(filters))))
            else questionCollection.aggregate(listOf())

        return res.map { questionDoc -> getQuestionFromDoc(questionDoc) }.toList()
    }

    override fun addQuestion(uid: String, groupId: String, question: Question): String? {
        val objectId = ObjectId()
        val questionDoc = Document("_id", objectId)
        questionDoc["question"] = question.question
        questionDoc["details"] = question.details
        questionDoc["questionGroupId"] = ObjectId(groupId)

        val res = mongoDbClient.getDb()
                .getCollection(QUESTIONS_COLLECTION)
                .insertOne(questionDoc)

        return if (res.wasAcknowledged()) objectId.toHexString() else null
    }

    override fun updateQuestion(uid: String, groupId: String, question: Question): String? {
        val res = mongoDbClient.getDb()
                .getCollection(QUESTIONS_COLLECTION)
                .updateOne(eq(OBJECT_ID_FIELD, ObjectId(question.id)), listOf(
                        set("question", question.question),
                        set("details", question.details),
                ))

        return if (res.modifiedCount >= 1) question.id else null
    }

    override fun getRandomQuestion(uid: String, groupId: String): Question? {
        val questionCollection = mongoDbClient.getDb().getCollection(QUESTIONS_COLLECTION)

        val randomQuestionDoc = questionCollection
                .aggregate(listOf(
                        match(eq("questionGroupId", ObjectId(groupId))),
                        sample(1)))
                .asIterable()
                .firstOrNull()

        return if (randomQuestionDoc != null)
            getQuestionFromDoc(randomQuestionDoc)
            else null
    }

    private fun getQuestionFromDoc(doc: Document): Question {
        val id = doc.getObjectId("_id")
        val question = doc.getOrDefault("question", null) as String?
        val details = doc.getOrDefault("details", null) as String?
        val questionGroupId = doc.getObjectId("questionGroupId")

        return Question(
                id.toHexString(),
                question,
                details,
                questionGroupId.toHexString()
        )
    }
}
