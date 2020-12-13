package ru.itmo.controller.auth

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import ru.itmo.autoconfigure.properties.JwtProperties
import ru.itmo.controller.model.request.AuthRequest
import ru.itmo.controller.model.response.AuthResponse
import ru.itmo.dao.UserDao
import ru.itmo.model.entity.User
import ru.itmo.service.JwtUserDetailsService
import ru.itmo.util.JwtTokenUtils


@CrossOrigin
@RestController
@RequestMapping(path = ["/auth"])
class JwtAuthenticationController(
    jwtProperties: JwtProperties,

    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: JwtUserDetailsService,

    private val userDao: UserDao
) {

    private val jwtTokenUtil: JwtTokenUtils = JwtTokenUtils(jwtProperties)

    @PostMapping(path = ["/register"])
    fun register(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        val (email, password) = authRequest
        val encryptedPassword = passwordEncoder.encode(password)

        if (userDao.findByEmail(email) != null) {
            throw BadCredentialsException("User with given email already exists")
        }

        val newUserEntity = userDao.save(User(email = email, password = encryptedPassword))
        authenticate(email, password)

        val serviceUser = JwtUserDetailsService.entityToVoModel(newUserEntity)
        val token = jwtTokenUtil.generateToken(serviceUser)

        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping(path = ["/login"])
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
            val (email, password) = authRequest
        authenticate(email, password)

        val serviceUser = userDetailsService.loadUserByUsername(email)
        val token = jwtTokenUtil.generateToken(serviceUser)

        return ResponseEntity.ok(AuthResponse(token))
    }

    private fun authenticate(email: String, password: String) {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))
    }

}