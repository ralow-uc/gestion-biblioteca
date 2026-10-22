package com.biblioteca.gestionbiblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce las excepciones de la aplicacion en respuestas HTTP con un cuerpo claro,
 * para que Postman muestre el motivo exacto del error.
 */
@RestControllerAdvice
public class ManejadorGlobalDeErrores {

    @ExceptionHandler(LibroNoEncontradoException.class)
    public ProblemDetail manejarLibroNoEncontrado(LibroNoEncontradoException ex) {
        ProblemDetail detalle = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detalle.setTitle("Libro no encontrado");
        detalle.setDetail(ex.getMessage());
        return detalle;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail detalle = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detalle.setTitle("Datos del libro invalidos");
        detalle.setDetail("Revisa los campos indicados en 'errores'");
        detalle.setProperty("errores", errores);
        return detalle;
    }
}
