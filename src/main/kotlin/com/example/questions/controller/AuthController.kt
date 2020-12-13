package com.example.questions.controller

import com.example.questions.ApiResult
import com.example.questions.service.UserService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
@RequestMapping("/auth")
class AuthController(
        private val userService: UserService
): Logging {
    data class SignInReq(val uid: String? = null)
    data class SignInRes(val signedIn: Boolean)

    @PostMapping("/signIn")
    fun signIn(@RequestBody req: SignInReq): ApiResult {
        logger.info("Sign in, uid: ${req.uid}")

        if (req.uid == null) {
            return ApiResult(true, SignInRes(false))
        }

        val res = userService.createUserIfNotExists(req.uid)

        return ApiResult(true, SignInRes(res))
    }
}