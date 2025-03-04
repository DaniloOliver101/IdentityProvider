package br.com.IdentityProvider.exception

import br.com.IdentityProvider.model.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse("Erro de Execução", ex.message)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
    //error UsernameNotFoundException
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(ex: UsernameNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse("Usuário não encontrado", ex.message)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
//error UnauthorizedException

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse("Code: 7", ex.message)
        return ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED)
    }





    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse("Code: 1", ex.message)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
