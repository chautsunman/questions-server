package com.example.questions.security

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfiguration: WebSecurityConfigurerAdapter(), Logging {
    override fun configure(http: HttpSecurity) {
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/api").authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()

        logger.info("configured security")
    }
}
