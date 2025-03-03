package br.com.IdentityProvider.model.dto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "Username não pode ser vazio")
    @field:Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
    val username: String,

    @field:NotBlank(message = "Password não pode ser vazio")
    @field:Size(min = 6, message = "Password deve ter no mínimo 6 caracteres")
    val password: String
)
