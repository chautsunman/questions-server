package com.example.questions.service.data

interface PojoDbMapper<PojoClass, DbClass> {
    fun encode(obj: PojoClass): DbClass

    fun decode(obj: DbClass): PojoClass
}
