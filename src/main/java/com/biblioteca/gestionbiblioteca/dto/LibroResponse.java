package com.biblioteca.gestionbiblioteca.dto;

import com.biblioteca.gestionbiblioteca.model.Libro;

/**
 * Representacion de un libro devuelta por el microservicio.
 */
public record LibroResponse(
        Long id,
        String titulo,
        String autor,
        Integer anioPublicacion,
        String genero) {

    public static LibroResponse desde(Libro libro) {
        return new LibroResponse(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getAnioPublicacion(),
                libro.getGenero());
    }
}
