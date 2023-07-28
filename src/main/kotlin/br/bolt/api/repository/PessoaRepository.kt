package br.bolt.api.repository

import br.bolt.api.model.Pessoa
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author Washington Antunes for wTI on 27/07/2023
 */
interface PessoaRepository : JpaRepository<Pessoa, String> {

    fun findByNomeContaining(nome: String, pageable: Pageable): Page<Pessoa>
}