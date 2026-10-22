package com.biblioteca.gestionbiblioteca.repository;

import com.biblioteca.gestionbiblioteca.model.Libro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprueba el mapeo JPA de la entidad Libro y las consultas derivadas
 * del repositorio contra una base de datos real (H2 en modo Oracle).
 */
@DataJpaTest
@ActiveProfiles("test")
class LibroRepositoryTest {

    @Autowired
    private LibroRepository libroRepository;

    @Test
    @DisplayName("JPA genera el ID con la secuencia y persiste los cinco atributos")
    void guardaYRecuperaElLibro() {
        Libro guardado = libroRepository.save(
                new Libro(null, "Ficciones", "Jorge Luis Borges", 1944, "Cuento"));

        assertThat(guardado.getId()).isNotNull();

        Libro recuperado = libroRepository.findById(guardado.getId()).orElseThrow();
        assertThat(recuperado.getTitulo()).isEqualTo("Ficciones");
        assertThat(recuperado.getAutor()).isEqualTo("Jorge Luis Borges");
        assertThat(recuperado.getAnioPublicacion()).isEqualTo(1944);
        assertThat(recuperado.getGenero()).isEqualTo("Cuento");
    }

    @Test
    @DisplayName("Las consultas derivadas filtran por titulo y por genero")
    void consultasDerivadas() {
        libroRepository.save(new Libro(null, "Clean Code", "Robert C. Martin", 2008, "Informatica"));
        libroRepository.save(new Libro(null, "Clean Architecture", "Robert C. Martin", 2017, "Informatica"));
        libroRepository.save(new Libro(null, "Rayuela", "Julio Cortazar", 1963, "Novela"));

        List<Libro> porTitulo = libroRepository.findByTituloContainingIgnoreCase("clean");
        List<Libro> porGenero = libroRepository.findByGeneroIgnoreCase("novela");

        assertThat(porTitulo).hasSize(2);
        assertThat(porGenero).hasSize(1);
        assertThat(porGenero.getFirst().getTitulo()).isEqualTo("Rayuela");
    }
}
