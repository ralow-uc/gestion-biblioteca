package com.biblioteca.gestionbiblioteca.service;

import com.biblioteca.gestionbiblioteca.dto.LibroRequest;
import com.biblioteca.gestionbiblioteca.dto.LibroResponse;

import java.util.List;

/**
 * Contrato de la capa de servicio con las operaciones CRUD sobre libros.
 */
public interface LibroService {

    /** Devuelve todos los libros registrados. */
    List<LibroResponse> listar();

    /** Devuelve los libros cuyo titulo contiene el texto indicado. */
    List<LibroResponse> buscarPorTitulo(String titulo);

    /** Obtiene la informacion de un libro por su ID. */
    LibroResponse obtenerPorId(Long id);

    /** Agrega un nuevo libro a la base de datos. */
    LibroResponse crear(LibroRequest solicitud);

    /** Actualiza la informacion de un libro existente. */
    LibroResponse actualizar(Long id, LibroRequest solicitud);

    /** Elimina un libro de la base de datos por su ID. */
    void eliminar(Long id);
}
