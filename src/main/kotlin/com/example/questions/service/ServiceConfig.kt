package com.example.questions.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfig {
    @Bean
    fun mongoDbClient(): MongoDbClient {
        return MongoDbClient()
    }

    @Bean
    fun questionsService(
            mongoDbClient: MongoDbClient,
            questionGroupsService: QuestionGroupsService
    ): QuestionsService {
        return QuestionsServiceImpl(mongoDbClient, questionGroupsService)
    }

    @Bean
    fun questionGroupsService(
            mongoDbClient: MongoDbClient
    ): QuestionGroupsService {
        return QuestionGroupsServiceImpl(mongoDbClient)
    }

    @Bean
    fun userService(
            mongoDbClient: MongoDbClient,
            questionGroupsService: QuestionGroupsService
    ): UserService {
        return UserServiceImpl(mongoDbClient, questionGroupsService)
    }
}
