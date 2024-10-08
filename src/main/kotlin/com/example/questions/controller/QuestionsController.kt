package com.example.questions.controller

import com.example.questions.ApiResult
import com.example.questions.data.Question
import com.example.questions.security.UserPrincipal
import com.example.questions.service.QuestionsService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/questions")
class QuestionsController(
        private val questionsService: QuestionsService
): Logging {
    data class AddQuestionReqBody(val groupId: String? = null, val question: Question? = null)
    data class UpdateQuestionReqBody(val groupId: String? = null, val question: Question? = null)

    @GetMapping("/questions")
    fun questions(
            @RequestParam(name = "groupId", required = true) groupId: String,
            @RequestParam(name = "id", required = false) id: String?,
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ApiResult {
        val uid = userPrincipal.uid

        logger.info("Get questions, uid: $uid, groupId: $groupId, id: $id")

        val questions = questionsService.getQuestions(uid, groupId, id)

        return ApiResult(true, questions)
    }

    @PostMapping("/addQuestion")
    fun addQuestion(
            @RequestBody reqBody: AddQuestionReqBody,
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ApiResult {
        val uid = userPrincipal.uid

        if (reqBody.groupId == null) {
            logger.info("null group ID")
            return ApiResult(false, null)
        }
        if (reqBody.question == null) {
            logger.info("null question")
            return ApiResult(false, null)
        }

        logger.info("Add question, uid: $uid, groupId: ${reqBody.groupId}")

        val res = questionsService.addQuestion(uid, reqBody.groupId, reqBody.question)

        return ApiResult(true, res)
    }

    @PostMapping("/updateQuestion")
    fun updateQuestion(
            @RequestBody reqBody: UpdateQuestionReqBody,
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ApiResult {
        val uid = userPrincipal.uid

        if (reqBody.groupId == null) {
            logger.info("null group ID")
            return ApiResult(false, null)
        }
        if (reqBody.question == null || reqBody.question.id == null) {
            logger.info("null question or null question ID")
            return ApiResult(false, null)
        }

        logger.info("Update question, uid: $uid, ID: ${reqBody.question.id}")

        val res = questionsService.updateQuestion(uid, reqBody.groupId, reqBody.question)

        return ApiResult(true, res)
    }

    @GetMapping("/getRandomQuestion")
    fun getRandomQuestion(
            @RequestParam(name = "groupId", required = true) groupId: String,
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ApiResult {
        val uid = userPrincipal.uid

        logger.info("Get random question, uid: $uid")

        val randomQuestion = questionsService.getRandomQuestion(uid, groupId)

        return ApiResult(true, randomQuestion)
    }
}