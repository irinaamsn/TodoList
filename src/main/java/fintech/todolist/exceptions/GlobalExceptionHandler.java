package fintech.todolist.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import fintech.todolist.errors.ErrorDtoResponse;
import fintech.todolist.exceptions.base.GlobalTodolistException;
import fintech.todolist.exceptions.base.TodolistApiException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TodolistApiException.class)
    public ResponseEntity<ErrorDtoResponse> handleWeatherApiException(TodolistApiException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDtoResponse(e.getStatus().value(), e.getErrorMessage()));
    }

    @ExceptionHandler(GlobalTodolistException.class)
    public ResponseEntity<ErrorDtoResponse> handleGlobalWeatherException(GlobalTodolistException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorDtoResponse(e.getStatus().value(), e.getErrorMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDtoResponse> handleGlobalWeatherException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDtoResponse(HttpStatus.BAD_REQUEST.value(),
                        "Invalid data"));
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorDtoResponse> handleRequestNotPermittedException(RequestNotPermitted e) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorDtoResponse(HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Sorry...too many requests"));
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorDtoResponse> handleRequestNotPermittedException(TimeoutException e) {
        return ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .body(new ErrorDtoResponse(HttpStatus.GATEWAY_TIMEOUT.value(),
                        "Wait while we fulfill your request"));
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ErrorDtoResponse> handleJsonMappingException(JsonMappingException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDtoResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<ErrorDtoResponse> handleAccessDeniedException(CustomAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorDtoResponse(HttpStatus.FORBIDDEN.value(),
                        e.getErrorMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDtoResponse> handleSqlException(DataAccessException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDtoResponse(HttpStatus.BAD_REQUEST.value(),
                        "Invalid request"));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDtoResponse> handleUnknownException(Throwable e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDtoResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Unknown error"));
    }
}
