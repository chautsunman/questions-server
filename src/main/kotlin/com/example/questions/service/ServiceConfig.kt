package com.example.questions.service

import com.example.questions.service.data.QuestionGroupMapper
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
            mongoDbClient: MongoDbClient,
            questionGroupMapper: QuestionGroupMapper
    ): QuestionGroupsService {
        return QuestionGroupsServiceImpl(mongoDbClient, questionGroupMapper)
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

    @Bean
    fun questionGroupMapper(): QuestionGroupMapper {
        return QuestionGroupMapper()
    }
}
