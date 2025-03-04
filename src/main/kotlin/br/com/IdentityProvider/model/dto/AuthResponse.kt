package br.com.IdentityProvider.model.dto

data class AuthResponse(

    val data: Data
)


data class Data(
    val accessToken: String
)
