package br.com.apivotacao.controllers.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ApiException {

    private final String title;
    private final String message;
    private final Integer httpStatus;
    private final LocalDateTime timestamp;
}
