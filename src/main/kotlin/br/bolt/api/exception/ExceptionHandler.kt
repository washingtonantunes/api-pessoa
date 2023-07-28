package br.bolt.api.exception

import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import java.util.List

/**
 * @author Washington Antunes for wTI on 27/07/2023
 */
@ControllerAdvice
class ExceptionHandler(
    private val messageSource: MessageSource
): ResponseEntityExceptionHandler() {

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale())
        val mensagemDesenvolvedor = Optional.ofNullable(ex.cause).orElse(ex).toString()

        return handleExceptionInternal(ex, Erro(mensagemUsuario, mensagemDesenvolvedor), headers, HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResultDataAccessException(
        ex: EmptyResultDataAccessException,
        request: WebRequest?
    ): ResponseEntity<Any?>? {
        val mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale())
        val mensagemDesenvolvedor = ex.toString()
        val erros = List.of(Erro(mensagemUsuario, mensagemDesenvolvedor))
        return handleExceptionInternal(
            ex, erros, HttpHeaders(), HttpStatus.NOT_FOUND,
            request!!
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(
        ex: DataIntegrityViolationException?,
        request: WebRequest?
    ): ResponseEntity<Any?>? {
        val mensagemUsuario =
            messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale())
        val mensagemDesenvolvedor: String = ExceptionUtils.getRootCauseMessage(ex)
        val erros = List.of(Erro(mensagemUsuario, mensagemDesenvolvedor))
        return handleExceptionInternal(
            ex!!, erros, HttpHeaders(), HttpStatus.NOT_FOUND,
            request!!
        )
    }
}

data class Erro(val mensagemUsuario: String, val mensagemDesenvolvedor: String)
