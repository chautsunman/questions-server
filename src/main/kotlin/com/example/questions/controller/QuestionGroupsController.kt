package com.example.questions.controller

import com.example.questions.ApiResult
import com.example.questions.data.QuestionGroup
import com.example.questions.service.QuestionGroupsService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/questionGroups")
class QuestionGroupsController(
        private val questionGroupsService: QuestionGroupsService,
): Logging {
    data class AddQuestionGroupReqBody(val questionGroup: QuestionGroup? = null)
    data class UpdateQuestionGroupReqBody(val questionGroup: QuestionGroup? = null)

    @GetMapping("/questionGroups")
    fun questionGroups(
            @RequestParam(name = "uid", required = true) uid: String
    ): ApiResult {
        logger.info("Get question groups")

        val questionGroups = questionGroupsService.getQuestionGroups(uid)

        return ApiResult(true, questionGroups)
    }

    @PostMapping("/addQuestionGroup")
    fun addQuestion(@RequestBody reqBody: AddQuestionGroupReqBody): ApiResult {
        logger.info("Add question")

        if (reqBody.questionGroup == null) {
            return ApiResult(false, null)
        }

        val res = questionGroupsService.addQuestionGroup(reqBody.questionGroup, "uid")

        return ApiResult(true, res)
    }

    @PostMapping("/updateQuestionGroup")
    fun updateQuestion(@RequestBody reqBody: UpdateQuestionGroupReqBody): ApiResult {
        logger.info("Update question")

        if (reqBody.questionGroup == null) {
            return ApiResult(false, null)
        }

        val res = questionGroupsService.updateQuestionGroup(reqBody.questionGroup, "uid")

        return ApiResult(true, res)
    }
}
