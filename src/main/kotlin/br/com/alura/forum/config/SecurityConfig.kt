package br.com.alura.forum.config

import br.com.alura.forum.security.JWTAuthenticationFilter
import br.com.alura.forum.security.JWTLoginFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val configuration: AuthenticationConfiguration,
    private val jwtUtil: JWTUtil
) {
    @Bean
     fun filterChain(http: HttpSecurity) : SecurityFilterChain {

         http {
             //csrf é u mfiltro para proteger de ataques maliciosos
             csrf { disable() }
             authorizeRequests {
                 authorize(AntPathRequestMatcher("/topicos/**"), hasAuthority("LEITURA_ESCRITA"))
                 authorize(AntPathRequestMatcher( "/login", HttpMethod.POST.toString()), permitAll)
                 authorize("/h2-console/**", permitAll)
                 authorize(anyRequest, authenticated)
             }
             addFilterBefore(
                 JWTLoginFilter(authManager = configuration.authenticationManager, jwtUtil = jwtUtil,),
                 UsernamePasswordAuthenticationFilter::class.java)

             addFilterBefore(JWTAuthenticationFilter(jwtUtil = jwtUtil), UsernamePasswordAuthenticationFilter::class.java)

             sessionManagement {
                 sessionCreationPolicy = SessionCreationPolicy.STATELESS
             }
             headers { frameOptions { disable() } }
         }
         return http.build()
     }

    @Bean
    fun encoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}