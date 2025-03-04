package br.com.IdentityProvider.service

import br.com.IdentityProvider.model.Role
import br.com.IdentityProvider.model.User
import br.com.IdentityProvider.model.dto.AuthRequest
import br.com.IdentityProvider.model.dto.RegisterRequest
import br.com.IdentityProvider.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) {

    fun authenticate(request: AuthRequest): String {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        val userDetails = userDetailsService.loadUserByUsername(request.username)
        // Retorna o token JWT gerado
        return jwtService.generateToken(userDetails)
    }

    fun register(request: RegisterRequest) {
        if(userRepository. existsByUserName(request.username)){
            throw IllegalArgumentException("Username já existe")
        }
        val encodedPassword  = passwordEncoder.encode(request.password)
        val user = User(userName = request.username,
            passwordHash = encodedPassword,
            role = Role.user.ordinal,
        enabled =  true)
        userRepository.save(user)

    }
    fun validateToken(token: String ): String {
        val  userName =jwtService.extractUsername(token)
        val userDetails = userDetailsService.loadUserByUsername(userName)
        return if( jwtService.isTokenValid(token,userDetails)){
            token
        } else{
            "Token inválido"
        }
    }
}
