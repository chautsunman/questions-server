package com.example.questions.controller

import com.example.questions.ApiResult
import com.example.questions.data.User
import com.example.questions.security.UserPrincipal
import com.example.questions.service.UserService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val userService: UserService
): Logging {
    data class SignInRes(val signedIn: Boolean)

    @PostMapping("/signIn")
    fun signIn(
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ApiResult {
        logger.info("Sign in")

        val uid = userPrincipal.uid
        val email = userPrincipal.email
        logger.info("uid: $uid, email: $email")

        val user = User(uid, email)

        val res = userService.setUpUser(user)
        if (!res) {
            return ApiResult(true, SignInRes(false))
        }

        return ApiResult(true, SignInRes(true))
    }
}