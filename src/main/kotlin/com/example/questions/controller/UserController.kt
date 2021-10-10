package com.example.questions.controller

import com.example.questions.ApiResult
import com.example.questions.service.UserService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
        private val userService: UserService
): Logging {
    data class SignInRes(val signedIn: Boolean)

    @GetMapping("/searchUser")
    fun signIn(
            searchStr: String? = null
    ): ApiResult {
        logger.info("Search user, $searchStr")

        if (searchStr == null) {
            return ApiResult(true, null)
        }

        val user = userService.searchUser(searchStr)

        return ApiResult(true, user)
    }
}
