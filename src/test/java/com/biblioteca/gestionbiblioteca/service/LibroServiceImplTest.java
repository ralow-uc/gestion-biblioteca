package com.biblioteca.gestionbiblioteca.service;

import com.biblioteca.gestionbiblioteca.dto.LibroRequest;
import com.biblioteca.gestionbiblioteca.dto.LibroResponse;
import com.biblioteca.gestionbiblioteca.exception.LibroNoEncontradoException;
import com.biblioteca.gestionbiblioteca.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pruebas del CRUD completo a traves de la capa de servicio y del repositorio JPA.
 */
@SpringBootTest
@ActiveProfiles("test")
class LibroServiceImplTest {

    @Autowired
    private LibroService libroService;

    @Autowired
    private LibroRepository libroRepository;

    @BeforeEach
    void limpiarTabla() {
        libroRepository.deleteAll();
    }

    @Test
    @DisplayName("POST: agrega un libro con todos los atributos solicitados")
    void crearGuardaElLibro() {
        LibroResponse creado = libroService.crear(
                new LibroRequest("Rayuela", "Julio Cortazar", 1963, "Novela"));

        assertThat(creado.id()).isNotNull();
        assertThat(creado.titulo()).isEqualTo("Rayuela");
        assertThat(creado.autor()).isEqualTo("Julio Cortazar");
        assertThat(creado.anioPublicacion()).isEqualTo(1963);
        assertThat(creado.genero()).isEqualTo("Novela");
        assertThat(libroRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("GET: obtiene la informacion de un libro por su ID")
    void obtenerPorIdDevuelveElLibro() {
        Long id = libroService.crear(
                new LibroRequest("El Aleph", "Jorge Luis Borges", 1949, "Cuento")).id();

        LibroResponse encontrado = libroService.obtenerPorId(id);

        assertThat(encontrado.titulo()).isEqualTo("El Aleph");
        assertThat(encontrado.autor()).isEqualTo("Jorge Luis Borges");
    }

    @Test
    @DisplayName("GET: un ID inexistente lanza LibroNoEncontradoException")
    void obtenerPorIdInexistenteFalla() {
        assertThatThrownBy(() -> libroService.obtenerPorId(9999L))
                .isInstanceOf(LibroNoEncontradoException.class)
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("PUT: actualiza la informacion de un libro existente")
    void actualizarModificaElLibro() {
        Long id = libroService.crear(
                new LibroRequest("Pedro Paramo", "Juan Rulfo", 1955, "Novela")).id();

        LibroResponse actualizado = libroService.actualizar(
                id, new LibroRequest("Pedro Paramo", "Juan Rulfo", 1955, "Realismo magico"));

        assertThat(actualizado.id()).isEqualTo(id);
        assertThat(actualizado.genero()).isEqualTo("Realismo magico");
        assertThat(libroRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("DELETE: elimina un libro de la base de datos por su ID")
    void eliminarBorraElLibro() {
        Long id = libroService.crear(
                new LibroRequest("Ficciones", "Jorge Luis Borges", 1944, "Cuento")).id();

        libroService.eliminar(id);

        assertThat(libroRepository.existsById(id)).isFalse();
        assertThatThrownBy(() -> libroService.eliminar(id))
                .isInstanceOf(LibroNoEncontradoException.class);
    }

    @Test
    @DisplayName("GET: lista todos los libros y filtra por titulo")
    void listarYBuscarPorTitulo() {
        libroService.crear(new LibroRequest("Cien anos de soledad", "Gabriel Garcia Marquez", 1967, "Realismo magico"));
        libroService.crear(new LibroRequest("Clean Code", "Robert C. Martin", 2008, "Informatica"));

        List<LibroResponse> todos = libroService.listar();
        List<LibroResponse> filtrados = libroService.buscarPorTitulo("clean");

        assertThat(todos).hasSize(2);
        assertThat(filtrados).hasSize(1);
        assertThat(filtrados.getFirst().autor()).isEqualTo("Robert C. Martin");
    }
}
