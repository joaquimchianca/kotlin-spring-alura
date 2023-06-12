package br.com.alura.forum.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@EnableWebSecurity
@Configuration
class SecurityConfig {
    @Bean
     fun filterChain(http: HttpSecurity) : SecurityFilterChain {

         http {
             csrf { disable() }
             authorizeRequests {
                 authorize(AntPathRequestMatcher("/topicos/**"), hasAuthority("LEITURA_ESCRITA"))
                 authorize("/h2-console/**", permitAll)
                 authorize(anyRequest, authenticated)
             }
             sessionManagement {
                 sessionCreationPolicy = SessionCreationPolicy.STATELESS
             }
             headers { frameOptions { disable() } }
             httpBasic { }
         }
         return http.build()
     }

    @Bean
    fun encoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}