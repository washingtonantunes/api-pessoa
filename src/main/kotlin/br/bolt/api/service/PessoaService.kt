package br.bolt.api.service

import br.bolt.api.model.Pessoa
import br.bolt.api.repository.PessoaRepository
import org.springframework.beans.BeanUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

/**
 * @author Washington Antunes for wTI on 27/07/2023
 */
@Service
class PessoaService(
    private val pessoaRepository: PessoaRepository
) {
    fun atualizar(id: String, pessoa: Pessoa): Pessoa {
        val pessoSalva = pessoaRepository.findById(id).orElseThrow {
            EmptyResultDataAccessException(1)
        }

        BeanUtils.copyProperties(pessoa, pessoSalva, "id")

        return pessoaRepository.save(pessoa)
    }
}
