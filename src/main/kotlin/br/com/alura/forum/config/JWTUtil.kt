package br.com.alura.forum.config

import br.com.alura.forum.model.Role
import br.com.alura.forum.service.UsuarioService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.security.Key
import java.util.*

@Component
class JWTUtil(
    private val usuarioService: UsuarioService
) {

    private val expiration = 60000
    private val secret: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    fun generateToken(username: String, authorities: MutableList<Role>) : String? {
        return Jwts.builder()
            .setSubject(username)
            .claim("role", authorities)
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(secret, SignatureAlgorithm.HS512)
            .compact()
    }

    //checa se o token eh valido
    fun isValid(tokenDetail: String?): Boolean {
        return try {
            val parser = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
            parser.parseClaimsJws(tokenDetail)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getAuthentication(tokenDetail: String?) : Authentication {
        val parser = Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
        val username = parser.parseClaimsJws(tokenDetail).body.subject
        val user = usuarioService.loadUserByUsername(username)
        return UsernamePasswordAuthenticationToken(username, null, user.authorities)
    }
}