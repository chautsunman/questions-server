package com.example.questions

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class AppWebConfig(
        private val appProperties: AppProperties
) : WebMvcConfigurer, Logging {
    override fun addCorsMappings(registry: CorsRegistry) {
        val corsAllowedOrigin = appProperties.corsAllowedOrigin
        registry
                .addMapping("/api/**")
                .allowedOrigins(corsAllowedOrigin)
        logger.info("set cors allowed origin as ${corsAllowedOrigin}")
    }
}
