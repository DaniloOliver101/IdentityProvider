package br.com.IdentityProvider.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService {

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun extractUsername(token: String): String =
        extractClaim(token, Claims::getSubject)

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun generateToken(userDetails: UserDetails): String =
        generateToken(emptyMap(), userDetails)

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean =
        extractClaim(token, Claims::getExpiration).before(Date())

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(): Key {
        return try {
            // Tenta interpretar a chave configurada como Base64
            val keyBytes = Base64.getDecoder().decode(secret)
            if (keyBytes.size < 32)
                throw IllegalArgumentException("Chave insuficiente")
            Keys.hmacShaKeyFor(keyBytes)
        } catch (e: Exception) {
            // Gera uma nova chave segura se a configurada não for adequada
            val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
            println("Chave gerada dinamicamente (Base64): ${Base64.getEncoder().encodeToString(key.encoded)}")
            key
        }
    }
    fun refreshToken(token: String): String {
        val claims = extractAllClaims(token)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 5))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }


}
