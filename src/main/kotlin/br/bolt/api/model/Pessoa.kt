package br.bolt.api.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

/**
 * @author Washington Antunes for wTI on 27/07/2023
 */
@Entity
@Table(name = "pessoa")
data class Pessoa(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @NotNull
    val nome: String,
    val email: String,
    @NotNull
    @Column(name = "data_nascimento")
    val dataNascimento: LocalDate,
    val idade: Int
)