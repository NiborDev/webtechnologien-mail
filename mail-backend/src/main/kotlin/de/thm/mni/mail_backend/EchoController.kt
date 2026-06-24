package de.thm.mni.mail_backend

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EchoController {
    @GetMapping("/echo")
    fun echo(@RequestParam message: String): String {
        return "Echo: $message"
    }
}