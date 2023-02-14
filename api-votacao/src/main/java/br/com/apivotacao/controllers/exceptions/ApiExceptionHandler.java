package br.com.apivotacao.controllers.exceptions;

import br.com.apivotacao.exceptions.BusinessException;
import br.com.apivotacao.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<ApiException> haddlerBusinessException(BusinessException e, HttpServletRequest request) {

        ApiException apiException = ApiException.builder()
                .title("Requisição inválida")
                .message(e.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now()).build();

        log.error("Status Http: {}, message: {}\n", HttpStatus.BAD_REQUEST.value(), e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ApiException> haddlerBusinessException(ResourceNotFoundException e, HttpServletRequest request) {

        ApiException apiException = ApiException.builder()
                .title("Requisição inválida")
                .message(e.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now()).build();

        log.error("Status Http: {}, message: {}\n", HttpStatus.NOT_FOUND.value(), e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }
}
