package br.com.alura.forum.security

import br.com.alura.forum.config.JWTUtil
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(
    val jwtUtil: JWTUtil
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        //pegar o token da requisicao do usuario, para checar se ele tem
        //autorizacao para acessar o endpoint
        val token = request.getHeader("Authorization")
        val tokenDetail = getTokenDetail(token)

        if(jwtUtil.isValid(tokenDetail)) {
            //seta o contexto de autenticacao da aplicacao
            val authentication = jwtUtil.getAuthentication(tokenDetail)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)


    }

    //funcao que extrai o token do header da request
    private fun getTokenDetail(token: String?) : String? {
        return token?.let {jwt ->
            jwt.startsWith("Bearer ")
            jwt.substring(7, token.length)
        }
    }

}
