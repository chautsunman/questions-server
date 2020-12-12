package com.example.questions

import org.bson.Document
import org.bson.types.ObjectId
import java.util.*

class QuestionDocumentFactory {
    fun newDoc(question: Question): Document {
        val doc = getDoc(question)
        with(doc) {
            append("_id", ObjectId())
            replace("id", UUID.randomUUID().toString())
        }

        return doc
    }

    fun getDoc(question: Question): Document {
        val doc = Document()
        with(doc) {
            append("id", question.id)
            append("question", question.question)
        }

        return doc
    }
}