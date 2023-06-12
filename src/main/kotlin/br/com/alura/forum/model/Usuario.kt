package br.com.alura.forum.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Usuario(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val nome: String,
        val email: String,
        val password: String,
        @JsonIgnore
        @JoinColumn(name = "usuario_role")
        @ManyToMany(fetch = FetchType.EAGER)
        val role: MutableList<Role> = mutableListOf()
)
