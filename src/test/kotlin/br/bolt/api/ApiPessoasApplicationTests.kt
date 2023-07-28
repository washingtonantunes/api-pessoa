package br.bolt.api

import br.bolt.api.model.Pessoa
import br.bolt.api.repository.PessoaRepository
import com.fasterxml.jackson.databind.json.JsonMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApiPessoasApplicationTests {

    @Autowired lateinit var mockMvc: MockMvc

    @Autowired lateinit var pessoaRepository: PessoaRepository

    private val objectMapper = JsonMapper.builder()
        .findAndAddModules()
        .build();

    @Test
    fun `test find all`() {
        pessoaRepository.save(
            Pessoa(
                nome = "João da Silva",
                email = "joao@example.com",
                dataNascimento = LocalDate.of(1990, 5, 15),
                idade = 31
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.get("/pessoas"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.content[0].id").isNumber)
            .andExpect(jsonPath("\$.content[0].nome").isString)
            .andExpect(jsonPath("\$.content[0].email").isString)
            .andExpect(jsonPath("\$.content[0].dataNascimento").isString)
            .andExpect(jsonPath("\$.content[0].idade").isNumber)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test find by id`() {
        val pessoa = pessoaRepository.save(Pessoa(
            nome = "Maria Souza",
            email = "maria@example.com",
            dataNascimento = LocalDate.parse("1985-10-20"),
            idade = 36
        ))

        mockMvc.perform(MockMvcRequestBuilders.get("/pessoas/${pessoa.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id").value(pessoa.id))
            .andExpect(jsonPath("\$.nome").value(pessoa.nome))
            .andExpect(jsonPath("\$.email").value(pessoa.email))
            .andExpect(jsonPath("\$.idade").value(pessoa.idade))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create pessoa`() {
        val pessoa = Pessoa(
            nome = "Icaro Silva",
            email = "icaro@example.com",
            dataNascimento = LocalDate.parse("1991-10-21"),
            idade = 36
        )
        val json = objectMapper.writeValueAsString(pessoa)
        pessoaRepository.deleteAll()
        mockMvc.perform(MockMvcRequestBuilders.post("/pessoas")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("\$.nome").value(pessoa.nome))
            .andExpect(jsonPath("\$.email").value(pessoa.email))
            .andExpect(jsonPath("\$.idade").value(pessoa.idade))
            .andDo(MockMvcResultHandlers.print())

        Assertions.assertFalse(pessoaRepository.findAll().isEmpty())
    }

    @Test
    fun `test update pessoa`() {
        val pessoa = pessoaRepository.save(
            Pessoa(
                nome = "Fernando Oliveira",
                email = "fernando@example.com",
                dataNascimento = LocalDate.of(1992, 7, 25),
                idade = 31
            )
        ).copy(nome = "Updated")
        val json = objectMapper.writeValueAsString(pessoa)
        mockMvc.perform(MockMvcRequestBuilders.put("/pessoas/${pessoa.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.nome").value(pessoa.nome))
            .andExpect(jsonPath("\$.email").value(pessoa.email))
            .andExpect(jsonPath("\$.dataNascimento").value(pessoa.dataNascimento.toString()))
            .andDo(MockMvcResultHandlers.print())

        val findById = pessoaRepository.findById(pessoa.id.toString())
        Assertions.assertTrue(findById.isPresent)
        Assertions.assertEquals(pessoa.nome, findById.get().nome)
    }

    @Test
    fun `test delete pessoa`() {
        val pessoa = pessoaRepository
            .save(Pessoa(
                nome = "Washington Oliveira",
                email = "washington@example.com",
                dataNascimento = LocalDate.of(1992, 7, 25),
                idade = 31
            ))
        mockMvc.perform(MockMvcRequestBuilders.delete("/pessoas/${pessoa.id}"))
            .andExpect(status().isNoContent)
            .andDo(MockMvcResultHandlers.print())

        val findById = pessoaRepository.findById(pessoa.id.toString())
        Assertions.assertFalse(findById.isPresent)
    }
}
