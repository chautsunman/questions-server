package com.example.questions

import org.apache.logging.log4j.kotlin.logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class QuestionsApplication

fun main(args: Array<String>) {
	val logger = logger("main")

	runApplication<QuestionsApplication>(*args)
	logger.info("App started")
}
