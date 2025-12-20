package com.susana_garcia.payment_gateway.infraestructure.exception;

import com.susana_garcia.payment_gateway.domain.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionNotFoundException.class)
    public ProblemDetail handleTransactionNotFoundException(TransactionNotFoundException ex){
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
                problemDetail.setTitle("Transação não encontrada");
                return problemDetail;
    }

    //Mapeamento de erro de dados (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex){
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
                problemDetail.setTitle("Erro de validação dos campos. ");
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach((fieldError) -> {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());

                });

                problemDetail.setProperty("erros_campos", errors);
                return problemDetail;
    }

}
