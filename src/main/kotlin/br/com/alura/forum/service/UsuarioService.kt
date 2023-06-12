package br.com.alura.forum.service

import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.model.UserDetail
import br.com.alura.forum.model.Usuario
import br.com.alura.forum.repository.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UsuarioService (
    private val usuarioRepository: UsuarioRepository
) : UserDetailsService {

    fun buscarPorId(id: Long): Usuario {
        return usuarioRepository.getOne(id)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val usuario = usuarioRepository.findByEmail(username) ?: throw NotFoundException("Usuário não encontrado")
        return UserDetail(usuario)
    }


}
