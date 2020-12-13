package com.example.questions

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.apache.logging.log4j.kotlin.logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QuestionsApplication

fun main(args: Array<String>) {
	val logger = logger("main")

	runApplication<QuestionsApplication>(*args)
	logger.info("App started")

	// initialize Firebase
	val options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.getApplicationDefault())
			.build()
	FirebaseApp.initializeApp(options)
	logger.info("initialized Firebase")
}
