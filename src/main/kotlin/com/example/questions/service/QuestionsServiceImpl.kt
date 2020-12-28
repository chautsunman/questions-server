package com.example.questions.service

import com.example.questions.data.Question
import com.example.questions.service.data.QuestionMapper
import com.example.questions.service.data.QuestionObj
import com.mongodb.client.model.Aggregates.match
import com.mongodb.client.model.Aggregates.sample
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import org.bson.conversions.Bson
import org.bson.types.ObjectId

class QuestionsServiceImpl(
        private val mongoDbClient: MongoDbClient,
        private val questionMapper: QuestionMapper
) : QuestionsService {
    override fun getQuestions(groupId: String?, id: String?): List<Question> {
        val questionCollection = mongoDbClient.getDb()
                .getCollection(QUESTIONS_COLLECTION, QuestionObj::class.java)

        val filters: ArrayList<Bson> = ArrayList()
        if (groupId != null) {
            filters.add(eq("questionGroupId", ObjectId(groupId)))
        }
        if (id != null) {
            filters.add(eq(OBJECT_ID_FIELD, ObjectId(id)))
        }

        val res = if (filters.isNotEmpty())
            questionCollection.find(and(filters))
            else questionCollection.find()
        val questions = res.map { questionObj -> questionMapper.decode(questionObj) }.toList()

        return questions
    }

    override fun addQuestion(groupId: String, question: Question): String? {
        val _question = question.copy(questionGroupId = groupId)
        val questionObj = questionMapper.encode(_question)

        val res = mongoDbClient.getDb()
                .getCollection(QUESTIONS_COLLECTION, QuestionObj::class.java)
                .insertOne(questionObj)

        return if (res.wasAcknowledged()) questionObj.id?.toHexString() else null
    }

    override fun updateQuestion(groupId: String, question: Question): String? {
        val _question = question.copy(questionGroupId = groupId)
        val questionObj = questionMapper.encode(_question)

        val res = mongoDbClient.getDb()
                .getCollection(QUESTIONS_COLLECTION, QuestionObj::class.java)
                .replaceOne(eq(OBJECT_ID_FIELD, questionObj.id), questionObj)

        return if (res.modifiedCount >= 1) question.id else null
    }

    override fun getRandomQuestion(groupId: String): Question? {
        val questionCollection = mongoDbClient.getDb()
                .getCollection(QUESTIONS_COLLECTION, QuestionObj::class.java)

        val randomQuestionObj = questionCollection
                .aggregate(listOf(
                        match(eq("questionGroupId", ObjectId(groupId))),
                        sample(1)))
                .asIterable()
                .firstOrNull()

        return if (randomQuestionObj != null)
            questionMapper.decode(randomQuestionObj)
            else null
    }
}
