package com.example.questions.service

import com.example.questions.service.data.QuestionMapper
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
            questionMapper: QuestionMapper
    ): QuestionsService {
        return QuestionsServiceImpl(mongoDbClient, questionMapper)
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

    @Bean
    fun questionMapper(): QuestionMapper {
        return QuestionMapper()
    }
}
