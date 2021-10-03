package com.example.questions

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("questions")
data class AppProperties(
        var corsAllowedOrigin: String
) {
}
