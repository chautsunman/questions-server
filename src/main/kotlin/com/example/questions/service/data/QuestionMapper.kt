package com.example.questions.service.data

import com.example.questions.data.Question
import org.bson.types.ObjectId

class QuestionMapper: PojoDbMapper<Question, com.example.questions.service.data.QuestionObj> {
    override fun encode(obj: Question): com.example.questions.service.data.QuestionObj {
        return QuestionObj(
                if (obj.id != null) ObjectId(obj.id) else ObjectId(),
                obj.question,
                if (obj.questionGroupId != null) ObjectId(obj.questionGroupId) else null
        )
    }

    override fun decode(obj: com.example.questions.service.data.QuestionObj): Question {
        return Question(
                obj.id?.toHexString(),
                obj.question,
                obj.questionGroupId?.toHexString()
        )
    }
}
