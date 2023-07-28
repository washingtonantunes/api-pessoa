package br.bolt.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiPessoasApplication

fun main(args: Array<String>) {
	runApplication<ApiPessoasApplication>(*args)
}
