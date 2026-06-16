package com.climaquest.cqapi.exception;

import com.climaquest.cqapi.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Trata os ResponseStatusException lançados nos services (404, 409, etc.)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        var status = HttpStatus.valueOf(ex.getStatusCode().value());
        var body = ErrorResponse.of(status.value(), status.getReasonPhrase(), ex.getReason());
        return ResponseEntity.status(status).body(body);
    }

    // Trata falhas do @Valid nos DTOs de request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();

        var body = ErrorResponse.of(400, "Bad Request", messages);
        return ResponseEntity.badRequest().body(body);
    }

    // Trata JSON malformado no corpo da requisição
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        var body = ErrorResponse.of(400, "Bad Request", "Corpo da requisição inválido ou malformado");
        return ResponseEntity.badRequest().body(body);
    }

    // Trata tipo errado em path variable (ex: texto no lugar de UUID)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var message = "Parâmetro '" + ex.getName() + "' com valor inválido: '" + ex.getValue() + "'";
        var body = ErrorResponse.of(400, "Bad Request", message);
        return ResponseEntity.badRequest().body(body);
    }

    // Trata violação de constraint única (ex: codename duplicado)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        var body = ErrorResponse.of(409, "Conflict", "Já existe um registro com esses dados (verifique campos únicos como o codinome)");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // Catch-all — qualquer exceção não tratada acima vira 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Erro inesperado: {}", ex.getMessage(), ex);
        var body = ErrorResponse.of(500, "Internal Server Error", "Erro interno inesperado");
        return ResponseEntity.internalServerError().body(body);
    }
}