package br.com.alura.forum.security

import br.com.alura.forum.config.JWTUtil
import br.com.alura.forum.model.Credentials
import br.com.alura.forum.model.UserDetail
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTLoginFilter(
    private val authManager: AuthenticationManager,
    private val jwtUtil: JWTUtil) :
    UsernamePasswordAuthenticationFilter() {

        //funcao para tentativa de autenticar o usuario
    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication {
            //pegar a requisicao e extrair username e password
            val(username, password) =
                ObjectMapper().readValue(request?.inputStream, Credentials::class.java)
            //atraves desse token vamos autenticar esse username e password
            val token =
                UsernamePasswordAuthenticationToken(username, password)
            //autentica o token
            return authManager.authenticate(token)
        }

    //o usuario existe, ele ja esta autenticado - gerar
    //o token para nao ficar fazendo login toda hora
    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val user = (authResult?.principal as UserDetail)
        val token = jwtUtil.generateToken(user.username, user.authorities)
        response?.addHeader("Authorization", "Bearer ${token}")
    }
}
