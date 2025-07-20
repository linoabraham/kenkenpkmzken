package com.gestion.cuentas.streaming.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones cuando una entidad no se encuentra en la base de datos.
     * Generalmente ocurre al buscar por un ID que no existe.
     * Devuelve un estado HTTP 404 (Not Found).
     *
     * @param ex      La excepción lanzada (EntityNotFoundException).
     * @param request La solicitud web actual.
     * @return Una ResponseEntity que contiene el ErrorResponse y el estado HTTP.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de argumentos ilegales, usadas para validaciones de negocio.
     * Por ejemplo: correo duplicado, asignación de una cuenta ya activa, etc.
     * Devuelve un estado HTTP 400 (Bad Request).
     *
     * @param ex      La excepción lanzada (IllegalArgumentException).
     * @param request La solicitud web actual.
     * @return Una ResponseEntity que contiene el ErrorResponse y el estado HTTP.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Un manejador de excepciones genérico como último recurso.
     * Captura cualquier otra excepción no manejada específicamente.
     * Devuelve un estado HTTP 500 (Internal Server Error).
     *
     * @param ex      La excepción general lanzada.
     * @param request La solicitud web actual.
     * @return Una ResponseEntity que contiene el ErrorResponse y el estado HTTP.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Ocurrió un error inesperado: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}