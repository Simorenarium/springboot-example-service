package coffee.michel.usermanager.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception

/**
 * This advice is will handle all known exceptions
 * and create responses with the corresponding http status codes.
 */
@RestControllerAdvice
internal class ExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    @ExceptionHandler(Exception::class)
    fun handleAmbiguousException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.error(ex) { "An unhandled Exception was thrown:" }

        return ResponseEntity.status(500).body(ex.message)
    }

    @ExceptionHandler(SubjectNotFoundException::class)
    fun handleDetailedException(ex: SubjectNotFoundException, request: WebRequest) =
        ResponseEntity.status(404).body(ex.message)

    @ExceptionHandler(UserGroupNotFoundException::class)
    fun handleDetailedException(ex: UserGroupNotFoundException, request: WebRequest) =
        ResponseEntity.status(404).body(ex.message)

    @ExceptionHandler(SubjectAlreadyExistsException::class)
    fun handleDetailedException(ex: SubjectAlreadyExistsException, request: WebRequest) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ex.message)
}
