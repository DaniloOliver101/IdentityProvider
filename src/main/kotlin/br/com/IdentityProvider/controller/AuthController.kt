package br.com.IdentityProvider.controller

import br.com.IdentityProvider.model.dto.AuthRequest
import br.com.IdentityProvider.model.dto.RegisterRequest
import br.com.IdentityProvider.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/authenticate")
    fun authenticate(@Valid @RequestBody request: AuthRequest): ResponseEntity<String> =
        try {
            val token = authService.authenticate(request)
            ResponseEntity.ok(token)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message)
        }

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<String> =
        try {
            authService.register(request)
            ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso")
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
        }

}
