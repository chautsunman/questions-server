package com.example.questions

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class AppStartupRunner : ApplicationRunner, Logging {
    override fun run(args: ApplicationArguments) {
        logger.info("Post app startup")

        // initialize Firebase
        if (FirebaseApp.getApps().isEmpty()) {
            val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build()
            FirebaseApp.initializeApp(options)
            logger.info("initialized Firebase")
        } else {
            logger.error("firebase is initialized already")
        }
    }
}
