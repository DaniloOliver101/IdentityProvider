package br.com.IdentityProvider.model.dto

data class ErrorResponse(
    val error: String,
    val message: String?
)
