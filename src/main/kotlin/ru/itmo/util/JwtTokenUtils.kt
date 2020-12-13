package ru.itmo.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.autoconfigure.properties.JwtProperties
import java.util.*


class JwtTokenUtils(
    private val jwtProperties: JwtProperties
) {

    private val jwtParser: JwtParser = Jwts.parserBuilder()
        .setSigningKey(jwtProperties.secret)
        .requireIssuer(jwtProperties.issuer)
        .requireAudience(jwtProperties.audience)
        .build()

    fun getUsernameFromToken(token: String): String = getClaimFromToken(token) { claims -> claims.subject }

    fun getExpirationDateFromToken(token: String): Date = getClaimFromToken(token) { claims -> claims.expiration }

    fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims = jwtParser.parseClaimsJws(token).body

    private fun isTokenExpired(token: String): Boolean {
        val expirationDate = getExpirationDateFromToken(token)
        return expirationDate.before(Date())
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.username)
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuer(jwtProperties.issuer)
            .setAudience(jwtProperties.audience)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS512, jwtProperties.secret)
            .compact()
    }

    companion object {
        private const val JWT_TOKEN_VALIDITY = (5 * 60 * 60).toLong()
    }
}