package com.example.questions

import com.example.questions.controller.QuestionDocumentFactory
import com.example.questions.service.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun mongoDbClient(): MongoDbClient {
        return MongoDbClient()
    }

    @Bean
    fun questionsService(
            mongoDbClient: MongoDbClient,
            questionDocumentFactory: QuestionDocumentFactory
    ): QuestionsService {
        return QuestionsServiceImpl(mongoDbClient, questionDocumentFactory)
    }

    @Bean
    fun userService(
            mongoDbClient: MongoDbClient
    ): UserService {
        return UserServiceImpl(mongoDbClient)
    }

    @Bean
    fun questionDocumentFactory(): QuestionDocumentFactory {
        return QuestionDocumentFactory()
    }
}
