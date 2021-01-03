package com.example.questions.controller

import com.example.questions.ApiResult
import com.example.questions.service.UserService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/api/auth")
class AuthController(
        private val userService: UserService
): Logging {
    data class SignInRes(val signedIn: Boolean)

    @PostMapping("/signIn")
    fun signIn(
            principal: Principal
    ): ApiResult {
        logger.info("Sign in")

        val uid = principal.name
        logger.info("uid: $uid")

        val res = userService.setUpUser(uid)
        if (!res) {
            return ApiResult(true, SignInRes(false))
        }

        return ApiResult(true, SignInRes(true))
    }
}