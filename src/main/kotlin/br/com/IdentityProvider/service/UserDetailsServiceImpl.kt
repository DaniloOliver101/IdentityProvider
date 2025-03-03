package br.com.IdentityProvider.service



import br.com.IdentityProvider.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUserName(username)
            .orElseThrow { UsernameNotFoundException("Usuário não encontrado com o nome $username") }
    }
}
