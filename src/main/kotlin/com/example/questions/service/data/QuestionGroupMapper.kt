package com.example.questions.service.data

import com.example.questions.data.QuestionGroup
import org.bson.types.ObjectId

class QuestionGroupMapper: PojoDbMapper<QuestionGroup, com.example.questions.service.data.QuestionGroupObj> {
    override fun encode(obj: QuestionGroup): com.example.questions.service.data.QuestionGroupObj {
        return QuestionGroupObj(
                if (obj.id != null) ObjectId(obj.id) else ObjectId(),
                obj.name
        )
    }

    override fun decode(obj: com.example.questions.service.data.QuestionGroupObj): QuestionGroup {
        return QuestionGroup(
                obj.id?.toHexString(),
                obj.name
        )
    }
}
