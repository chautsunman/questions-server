package com.example.questions.service.data

import com.example.questions.data.QuestionGroup
import org.bson.types.ObjectId

class QuestionGroupMapper: PojoDbMapper<QuestionGroup, QuestionGroupObj> {
    override fun encode(obj: QuestionGroup): QuestionGroupObj {
        return QuestionGroupObj(
                if (obj.id != null) ObjectId(obj.id) else ObjectId(),
                obj.name,
                obj.users
        )
    }

    override fun decode(obj: QuestionGroupObj): QuestionGroup {
        return QuestionGroup(
                obj.id?.toHexString(),
                obj.name,
                obj.users
        )
    }
}
