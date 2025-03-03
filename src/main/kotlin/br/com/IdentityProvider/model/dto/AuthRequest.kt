package br.com.IdentityProvider.model.dto

import jakarta.validation.constraints.NotBlank

data class AuthRequest(
    @field:NotBlank(message = "Username não pode ser vazio")
    val username: String,
    @field:NotBlank(message = "Password não pode ser vazio")
    val password: String
)
