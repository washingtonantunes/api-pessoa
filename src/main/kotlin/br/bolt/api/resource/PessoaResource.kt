package br.bolt.api.resource

import br.bolt.api.model.Pessoa
import br.bolt.api.repository.PessoaRepository
import br.bolt.api.service.PessoaService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

/**
 * @author Washington Antunes for wTI on 27/07/2023
 */
@RestController
@RequestMapping("/pessoas")
class PessoaResource(
    private val pessoaRepository: PessoaRepository,
    private val pessoaService: PessoaService
) {

    @GetMapping
    fun pesquisar(@RequestParam(required = false, defaultValue = "") nome: String, pageable: Pageable): Page<Pessoa> =
        pessoaRepository.findByNomeContaining(nome, pageable)

    @GetMapping("/{id}")
    fun buscarPeloId(@PathVariable id: String): ResponseEntity<Pessoa> {
        return pessoaRepository.findById(id)
            .map{ pessoa -> ResponseEntity.ok(pessoa) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun criar(@Valid @RequestBody pessoa: Pessoa, response: HttpServletResponse): ResponseEntity<Pessoa> {
        val pessoaSalva = pessoaRepository.save(pessoa)

        val uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(pessoaSalva.id).toUri()
        response.setHeader("Location", uri.toASCIIString())

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: String, @Valid @RequestBody pessoa: Pessoa): ResponseEntity<Pessoa> {
        val pessoaSalva = pessoaService.atualizar(id, pessoa)
        return ResponseEntity.ok(pessoaSalva)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remover(@PathVariable id: String) {
        pessoaRepository.deleteById(id)
    }
}
