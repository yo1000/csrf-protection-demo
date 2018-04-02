package com.yo1000.csrf.protection.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

@SpringBootApplication
class CsrfProtectionDemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(CsrfProtectionDemoApplication::class.java, *args)
}

@Controller
@RequestMapping("/csrf/demo")
class CsrfController {
    companion object {
        val LOGGER = LoggerFactory.getLogger(CsrfController::class.java)
    }

    @ResponseBody
    @PostMapping("/protect")
    fun postProtectCsrf(): String {
        LOGGER.debug("protect")
        return "protect"
    }

    @ResponseBody
    @PostMapping("/ignore")
    fun postIgnoreCsrf(): String {
        LOGGER.debug("ignore")
        return "ignore"
    }

    @GetMapping("")
    fun getIndex(): String {
        return "index"
    }
}

class SkipLoginFilter : DefaultLoginPageGeneratingFilter() {
    override fun doFilter(req: ServletRequest?, res: ServletResponse?, chain: FilterChain?) {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null) {
            SecurityContextHolder.getContext().authentication = object : Authentication {
                private var authenticated = true

                override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                    return mutableListOf<GrantedAuthority>()
                }

                override fun setAuthenticated(p0: Boolean) {
                    authenticated = p0
                }

                override fun getName(): String {
                    return "user"
                }

                override fun getCredentials(): Any {
                    return ""
                }

                override fun getPrincipal(): Any {
                    return ""
                }

                override fun isAuthenticated(): Boolean {
                    return authenticated
                }

                override fun getDetails(): Any {
                    return ""
                }

            }
        }

        super.doFilter(req, res, chain)
    }
}

@Configuration
class CsrfConfig : WebSecurityConfigurerAdapter() {
    @Bean
    fun corsFilter(): FilterRegistrationBean {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("http://localhost:8082")
        config.addAllowedHeader(CorsConfiguration.ALL)
        config.addAllowedMethod(CorsConfiguration.ALL)

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        val bean = FilterRegistrationBean(CorsFilter(source))
        bean.order = Ordered.HIGHEST_PRECEDENCE;

        return bean;
    }

    override fun configure(http: HttpSecurity?) {
        super.configure(http)

        http?.let {
            it
                    .addFilterAfter(SkipLoginFilter(), RememberMeAuthenticationFilter::class.java)
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository())
                    .requireCsrfProtectionMatcher {
                        it.method.toUpperCase() == HttpMethod.POST.name && it.requestURI.endsWith("/protect")
                    }
        }
    }
}
