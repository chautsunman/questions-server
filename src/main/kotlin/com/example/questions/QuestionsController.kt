package com.example.questions

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/questions")
class QuestionsController(
        private val questionsService: QuestionsService
): Logging {
    @GetMapping("/questions")
    fun questions(): ApiResult {
        logger.info("Get questions")

        val questions = questionsService.getQuestions()

        return ApiResult(true, questions)
    }

    @PostMapping("/addQuestion")
    fun addQuestion(@RequestBody question: Question): ApiResult {
        logger.info("Add question")

        val res = questionsService.addQuestion(question)

        return ApiResult(true, res)
    }
}