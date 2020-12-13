package ru.itmo.service

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.itmo.dao.UserDao
import ru.itmo.model.entity.User
import ru.itmo.model.vo.ServiceUser


@Service
class JwtUserDetailsService(
    private val userDao: UserDao
): UserDetailsService {

    override fun loadUserByUsername(email: String): ServiceUser {
        return userDao.findByEmail(email)?.let { entity -> entityToVoModel(entity) } ?: run {
            throw UsernameNotFoundException("User not found with username: $email")
        }
    }

    companion object {
        @JvmStatic
        fun entityToVoModel(entity: User): ServiceUser = ServiceUser(
            id = entity.id,
            email = entity.email,
            password = entity.password
        )
    }
}