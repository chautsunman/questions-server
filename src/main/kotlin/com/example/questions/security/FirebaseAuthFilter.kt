package com.example.questions.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class FirebaseAuthFilter : OncePerRequestFilter() {
    private val bearerTokenResolver: BearerTokenResolver = DefaultBearerTokenResolver()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        // get token from Authorization header
        val token: String
        try {
            token = bearerTokenResolver.resolve(request)
        } catch (ex: OAuth2AuthenticationException) {
            return
        }
        if (token == null) {
            return
        }

        try {
            // verify token
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token, true)

            // construct principal and authentication
            val userPrincipal = UserPrincipal(decodedToken.uid, decodedToken.email)
            val authentication = UsernamePasswordAuthenticationToken(userPrincipal, null, emptyList())

            // authenticate user
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication

            if (logger.isDebugEnabled) {
                logger.debug("authenticated, uid: ${decodedToken.uid}")
            }

            chain.doFilter(request, response)
        } catch (ex: FirebaseAuthException) {
            return
        }
    }

    override fun shouldNotFilterErrorDispatch(): Boolean {
        return false
    }
}
