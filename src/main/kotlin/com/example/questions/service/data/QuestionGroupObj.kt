package com.example.questions.service.data

import org.bson.types.ObjectId

data class QuestionGroupObj (
        var id: ObjectId? = null,
        var name: String = ""
)
