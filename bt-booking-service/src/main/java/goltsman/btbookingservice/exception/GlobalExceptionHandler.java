package goltsman.btbookingservice.exception;

import goltsman.btbookingservice.exception.business.BookingConflictException;
import goltsman.btbookingservice.exception.business.ExternalServiceUnavailableException;
import goltsman.btbookingservice.exception.business.ResourceAlreadyExistsException;
import goltsman.btbookingservice.exception.business.RestaurantNotFoundException;
import goltsman.btbookingservice.exception.business.TableNotAvailableException;
import goltsman.btbookingservice.exception.business.TableNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //    InvalidDataAccessResourceUsageException, DateTimeParseException

    private ResponseEntity<HttpErrorResponse> buildErrorResponse(HttpStatus status,
                                                                 String type,
                                                                 String message,
                                                                 Exception ex) {
        log.error(type, ex);
        HttpErrorResponse errorResponse = new HttpErrorResponse(status.value(), type, message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        String errorMessage = String.join(", ", errorMessages);
        return buildErrorResponse(HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessage, ex);
    }

    @ExceptionHandler({
            NumberFormatException.class,
            IllegalArgumentException.class,
            MissingPathVariableException.class,
            ConstraintViolationException.class,
            IllegalStateException.class,
            DateTimeParseException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<HttpErrorResponse> handleBadRequest(Exception ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                StringUtils.hasText(ex.getMessage()) ?
                        ex.getMessage() : "Неправильные аргументы запроса",
                ex
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Некорректный формат запроса. Проверьте JSON и кавычки.",
                ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpErrorResponse> handleAuthenticationException(BadCredentialsException ex) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Отсутствует или некорректный токен аунтификации",
                ex
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                StringUtils.hasText(ex.getMessage()) ?
                        ex.getMessage() : "Недостаточно прав для выполнения данного действия",
                ex
        );
    }

    @ExceptionHandler({EntityNotFoundException.class, RestaurantNotFoundException.class, TableNotFoundException.class})
    public ResponseEntity<HttpErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                StringUtils.hasText(ex.getMessage()) ?
                        ex.getMessage() : "Запрашиваемый ресурс не найден",
                ex
        );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<HttpErrorResponse> handleConflict(ResourceAlreadyExistsException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.getReasonPhrase(),
                StringUtils.hasText(ex.getMessage()) ?
                        ex.getMessage() : "Ресурс уже существует",
                ex
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(TableNotAvailableException.class)
    public ResponseEntity<HttpErrorResponse> handleTableNotAvailable(TableNotAvailableException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.getReasonPhrase(),
                StringUtils.hasText(ex.getMessage()) ?
                        ex.getMessage() : "Стол не доступен для бронирования",
                ex
        );
    }

    @ExceptionHandler(BookingConflictException.class)
    public ResponseEntity<HttpErrorResponse> handleBookingConflict(BookingConflictException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.getReasonPhrase(),
                StringUtils.hasText(ex.getMessage()) ?
                        ex.getMessage() : "Столик на указанное время забронирован",
                ex
        );
    }

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<HttpErrorResponse> handleDataAccessResourceUsage(InvalidDataAccessResourceUsageException ex) {
        log.error("Ошибка доступа к данным", ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Database Error",
                "Ошибка при работе с базой данных. Неправильные аргументы запроса",
                ex
        );
    }

    @ExceptionHandler({Exception.class, ExternalServiceUnavailableException.class})
    public ResponseEntity<HttpErrorResponse> handleEntityInternalServerException(Exception ex) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Неизвестная ошибка сервера. Попробуйте снова",
                ex);
    }

}
