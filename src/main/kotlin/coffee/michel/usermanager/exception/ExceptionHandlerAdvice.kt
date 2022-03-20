package coffee.michel.usermanager.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * This advice is will handle all known exceptions
 * and create responses with the corresponding http status codes.
 */
@RestControllerAdvice
class ExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(
        SubjectNotFoundException::class
    )
    fun handleDetailedException(ex: SubjectNotFoundException, request: WebRequest) =
        // TODO maybe consider storing the id in the exception instead of trusting the message
        ResponseEntity.status(404).body(ex.message)
}
