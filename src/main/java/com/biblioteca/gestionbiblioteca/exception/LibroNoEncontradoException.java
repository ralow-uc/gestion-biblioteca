package com.biblioteca.gestionbiblioteca.exception;

/**
 * Se lanza cuando se solicita un libro cuyo ID no existe en la base de datos.
 */
public class LibroNoEncontradoException extends RuntimeException {

    public LibroNoEncontradoException(Long id) {
        super("No existe un libro con el ID " + id);
    }
}
