package com.example.questions

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun questionsService(): QuestionsService {
        return QuestionsServiceImpl()
    }
}
