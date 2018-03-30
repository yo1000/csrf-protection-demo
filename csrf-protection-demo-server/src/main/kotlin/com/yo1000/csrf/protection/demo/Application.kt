package com.yo1000.csrf.protection.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@SpringBootApplication
class CsrfProtectionDemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(CsrfProtectionDemoApplication::class.java, *args)
}

@RestController
@RequestMapping("/csrf/demo")
class CsrfController {
    companion object {
        val LOGGER = LoggerFactory.getLogger(CsrfController::class.java)
    }

    @PostMapping("/protect")
    fun postProtectCsrf(): String {
        LOGGER.debug("protect")
        return "protect"
    }

    @PostMapping("/ignore")
    fun postIgnoreCsrf(): String {
        LOGGER.debug("ignore")
        return "ignore"
    }
}

@Configuration
class CsrfConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        super.configure(http)

        http?.let {
            it.csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository())
                    .requireCsrfProtectionMatcher {
                        it.method.toUpperCase() == HttpMethod.POST.name && it.requestURI.endsWith("/protect")
                    }
            .and()
            .cors().configurationSource(UrlBasedCorsConfigurationSource().also {
                it.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
            })
        }
    }
}