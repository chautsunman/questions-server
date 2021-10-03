package com.example.questions.controller

import com.example.questions.ApiResult
import com.example.questions.data.QuestionGroup
import com.example.questions.service.QuestionGroupsService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/questionGroups")
class QuestionGroupsController(
        private val questionGroupsService: QuestionGroupsService,
): Logging {
    data class AddQuestionGroupReqBody(val questionGroup: QuestionGroup? = null)
    data class UpdateQuestionGroupReqBody(val questionGroup: QuestionGroup? = null)

    @GetMapping("/questionGroups")
    fun questionGroups(
            @RequestParam(name = "id", required = false) id: String?,
            principal: Principal
    ): ApiResult {
        val uid = principal.name

        logger.info("Get question groups, uid: $uid, id: $id")

        val questionGroups = questionGroupsService.getQuestionGroups(uid, id)

        return ApiResult(true, questionGroups)
    }

    @PostMapping("/addQuestionGroup")
    fun addQuestion(
            @RequestBody reqBody: AddQuestionGroupReqBody,
            principal: Principal
    ): ApiResult {
        val uid = principal.name

        if (reqBody.questionGroup == null) {
            logger.info("null question group")
            return ApiResult(false, null)
        }

        logger.info("Add question group, uid: $uid")

        val res = questionGroupsService.addQuestionGroup(reqBody.questionGroup, uid)

        return ApiResult(true, res)
    }

    @PostMapping("/updateQuestionGroup")
    fun updateQuestion(
            @RequestBody reqBody: UpdateQuestionGroupReqBody,
            principal: Principal
    ): ApiResult {
        val uid = principal.name

        if (reqBody.questionGroup?.id == null) {
            logger.info("null question group or null question group ID")
            return ApiResult(false, null)
        }

        logger.info("Update question group, uid: $uid, ID: ${reqBody.questionGroup.id}")

        val res = questionGroupsService.updateQuestionGroup(reqBody.questionGroup, uid)

        return ApiResult(true, res)
    }
}
