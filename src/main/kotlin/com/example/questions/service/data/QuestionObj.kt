package com.example.questions.service.data

import org.bson.types.ObjectId

data class QuestionObj(
        var id: ObjectId? = null,
        var question: String = "",
        var questionGroupId: ObjectId? = null
)
