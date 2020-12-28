package com.example.questions.controller

import com.example.questions.ApiResult
import com.example.questions.data.Question
import com.example.questions.service.QuestionsService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/questions")
class QuestionsController(
        private val questionsService: QuestionsService
): Logging {
    data class AddQuestionReqBody(val groupId: String, val question: Question)
    data class UpdateQuestionReqBody(val groupId: String, val question: Question)

    @GetMapping("/questions")
    fun questions(
            @RequestParam(name = "groupId", required = false) groupId: String?,
            @RequestParam(name = "id", required = false) id: String?
    ): ApiResult {
        logger.info("Get questions")

        val questions = questionsService.getQuestions(groupId, id)

        return ApiResult(true, questions)
    }

    @PostMapping("/addQuestion")
    fun addQuestion(@RequestBody reqBody: AddQuestionReqBody): ApiResult {
        logger.info("Add question")

        val res = questionsService.addQuestion(reqBody.groupId, reqBody.question)

        return ApiResult(true, res)
    }

    @PostMapping("/updateQuestion")
    fun updateQuestion(@RequestBody reqBody: UpdateQuestionReqBody): ApiResult {
        logger.info("Add question")

        if (reqBody.question.id == null) {
            return ApiResult(false, null)
        }

        val res = questionsService.updateQuestion(reqBody.groupId, reqBody.question)

        return ApiResult(true, res)
    }

    @GetMapping("/getRandomQuestion")
    fun getRandomQuestion(
            @RequestParam(name = "groupId", required = true) groupId: String
    ): ApiResult {
        logger.info("Get random question")

        val randomQuestion = questionsService.getRandomQuestion(groupId)

        return ApiResult(true, randomQuestion)
    }
}