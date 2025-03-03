package br.com.IdentityProvider.filter


import br.com.IdentityProvider.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader = request.getHeader("Authorization")
        val jwtToken = if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else {
            null
        }

        if (jwtToken != null && SecurityContextHolder.getContext().authentication == null) {
            try {
                val username = jwtService.extractUsername(jwtToken)
                if (username != null) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    if (jwtService.isTokenValid(jwtToken, userDetails)) {
                        val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authenticationToken
                    }
                }
            } catch (ex: Exception) {
                // Registre a exceção se necessário
            }
        }
        filterChain.doFilter(request, response)
    }
}
