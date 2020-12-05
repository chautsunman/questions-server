package com.example.questions

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun questionsService(
            questionDocumentFactory: QuestionDocumentFactory
    ): QuestionsService {
        return QuestionsServiceImpl(questionDocumentFactory)
    }

    @Bean
    fun questionDocumentFactory(): QuestionDocumentFactory {
        return QuestionDocumentFactory()
    }
}
