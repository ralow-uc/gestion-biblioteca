package com.biblioteca.gestionbiblioteca.repository;

import com.biblioteca.gestionbiblioteca.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA de la entidad Libro.
 *
 * Al extender JpaRepository se obtienen las operaciones CRUD contra la base de datos
 * Oracle sin escribir SQL: findAll, findById, save, deleteById, existsById, etc.
 */
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    /** Busca libros cuyo titulo contenga el texto indicado, sin distinguir mayusculas. */
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    /** Busca los libros de un genero determinado. */
    List<Libro> findByGeneroIgnoreCase(String genero);
}
