package com.example.questions.controller

import com.example.questions.ApiResult
import com.example.questions.data.User
import com.example.questions.service.UserService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val userService: UserService
): Logging {
    data class SignInRes(val signedIn: Boolean)

    @PostMapping("/signIn")
    fun signIn(
            principal: JwtAuthenticationToken
    ): ApiResult {
        logger.info("Sign in")

        val uid = principal.name
        logger.info("uid: $uid")

        val claims = (principal.credentials as Jwt).claims
        val email = if (claims.containsKey("email")) claims["email"] else null

        val user = User(uid, email as String?)

        val res = userService.setUpUser(user)
        if (!res) {
            return ApiResult(true, SignInRes(false))
        }

        return ApiResult(true, SignInRes(true))
    }
}