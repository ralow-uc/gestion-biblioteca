package com.biblioteca.gestionbiblioteca.controller;

import com.biblioteca.gestionbiblioteca.dto.LibroRequest;
import com.biblioteca.gestionbiblioteca.dto.LibroResponse;
import com.biblioteca.gestionbiblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Controlador REST que maneja las solicitudes HTTP relacionadas con los libros.
 *
 * Endpoints expuestos:
 *   GET    /api/libros          Lista todos los libros (admite ?titulo= para filtrar).
 *   GET    /api/libros/{id}     Obtiene la informacion de un libro por su ID.
 *   POST   /api/libros          Agrega un nuevo libro a la base de datos.
 *   PUT    /api/libros/{id}     Actualiza la informacion de un libro existente.
 *   DELETE /api/libros/{id}     Elimina un libro de la base de datos por su ID.
 */
@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<List<LibroResponse>> listar(
            @RequestParam(name = "titulo", required = false) String titulo) {

        List<LibroResponse> libros = (titulo == null || titulo.isBlank())
                ? libroService.listar()
                : libroService.buscarPorTitulo(titulo);

        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<LibroResponse> crear(@Valid @RequestBody LibroRequest solicitud,
                                               UriComponentsBuilder uriBuilder) {
        LibroResponse creado = libroService.crear(solicitud);
        return ResponseEntity
                .created(uriBuilder.path("/api/libros/{id}").buildAndExpand(creado.id()).toUri())
                .body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody LibroRequest solicitud) {
        return ResponseEntity.ok(libroService.actualizar(id, solicitud));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
