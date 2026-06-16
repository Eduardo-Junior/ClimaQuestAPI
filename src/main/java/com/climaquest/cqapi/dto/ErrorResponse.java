// dto/ErrorResponse.java
package com.climaquest.cqapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        List<String> messages,
        LocalDateTime timestamp
) {
    // Construtor conveniente para erro com mensagem única
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, List.of(message), LocalDateTime.now());
    }

    // Construtor para múltiplas mensagens (erros de validação)
    public static ErrorResponse of(int status, String error, List<String> messages) {
        return new ErrorResponse(status, error, messages, LocalDateTime.now());
    }
}