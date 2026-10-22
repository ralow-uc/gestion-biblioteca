package com.biblioteca.gestionbiblioteca.service.impl;

import com.biblioteca.gestionbiblioteca.dto.LibroRequest;
import com.biblioteca.gestionbiblioteca.dto.LibroResponse;
import com.biblioteca.gestionbiblioteca.exception.LibroNoEncontradoException;
import com.biblioteca.gestionbiblioteca.model.Libro;
import com.biblioteca.gestionbiblioteca.repository.LibroRepository;
import com.biblioteca.gestionbiblioteca.service.LibroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion de la logica de negocio del CRUD de libros.
 *
 * Toda la interaccion con la base de datos Oracle se hace a traves de LibroRepository (JPA).
 */
@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> listar() {
        return libroRepository.findAll()
                .stream()
                .map(LibroResponse::desde)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(LibroResponse::desde)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponse obtenerPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new LibroNoEncontradoException(id));
        return LibroResponse.desde(libro);
    }

    @Override
    @Transactional
    public LibroResponse crear(LibroRequest solicitud) {
        Libro libro = new Libro(
                null,
                solicitud.titulo().trim(),
                solicitud.autor().trim(),
                solicitud.anioPublicacion(),
                solicitud.genero().trim());
        return LibroResponse.desde(libroRepository.save(libro));
    }

    @Override
    @Transactional
    public LibroResponse actualizar(Long id, LibroRequest solicitud) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new LibroNoEncontradoException(id));

        libro.setTitulo(solicitud.titulo().trim());
        libro.setAutor(solicitud.autor().trim());
        libro.setAnioPublicacion(solicitud.anioPublicacion());
        libro.setGenero(solicitud.genero().trim());

        return LibroResponse.desde(libroRepository.save(libro));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new LibroNoEncontradoException(id);
        }
        libroRepository.deleteById(id);
    }
}
