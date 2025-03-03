package br.com.IdentityProvider.repository

import br.com.IdentityProvider.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface  UserRepository: JpaRepository<User,Long>{
    fun findByUserName(userName: String): Optional<User>
    fun existsByUserName(userName: String): Boolean


}