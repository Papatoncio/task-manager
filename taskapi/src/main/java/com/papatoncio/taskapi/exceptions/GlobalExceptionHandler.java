package com.papatoncio.taskapi.exceptions;

import com.papatoncio.taskapi.dto.common.ApiResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // ------------------- ENDPOINT/RECURSO NO ENCONTRADO -------------------
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse handleNoResourceFound(NoResourceFoundException ex) {
        return new ApiResponse(false, null, "El recurso o endpoint solicitado no existe.", null);
    }

    // ------------------- VALIDACIONES -------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ApiResponse(false, null, "Errores de validación", errors);
    }

    // ------------------- ERROR GENÉRICO -------------------
    @ExceptionHandler(Exception.class)
    public ApiResponse handleGenericException(Exception ex) {
        return new ApiResponse(false, null, "Ocurrió un error en el servidor.", null);
    }
}
