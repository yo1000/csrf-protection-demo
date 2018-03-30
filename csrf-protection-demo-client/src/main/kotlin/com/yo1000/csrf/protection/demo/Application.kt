package com.yo1000.csrf.protection.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

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

    @GetMapping("")
    fun postProtectCsrf(): String {
        return "index"
    }
}
