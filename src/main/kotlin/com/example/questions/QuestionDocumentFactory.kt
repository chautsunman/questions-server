package com.example.questions

import org.bson.Document
import org.bson.types.ObjectId
import java.util.*

class QuestionDocumentFactory {
    fun getDoc(question: Question): Document {
        val doc = Document("_id", ObjectId())
        with(doc) {
            append("key", UUID.randomUUID().toString())
            append("question", question.question)
        }

        return doc
    }
}