package com.biblioteca.gestionbiblioteca.config;

import com.biblioteca.gestionbiblioteca.model.Libro;
import com.biblioteca.gestionbiblioteca.repository.LibroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Carga algunos libros de ejemplo al iniciar con el perfil "local" (H2 en memoria),
 * para poder probar de inmediato las solicitudes GET, PUT y DELETE en Postman.
 *
 * No se ejecuta con el perfil "oracle": alli los datos los administra la base real.
 */
@Component
@Profile("local")
public class DatosDeEjemplo implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatosDeEjemplo.class);

    private final LibroRepository libroRepository;

    public DatosDeEjemplo(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public void run(String... args) {
        if (libroRepository.count() > 0) {
            return;
        }

        libroRepository.saveAll(List.of(
                new Libro(null, "Cien anos de soledad", "Gabriel Garcia Marquez", 1967, "Realismo magico"),
                new Libro(null, "La casa de los espiritus", "Isabel Allende", 1982, "Realismo magico"),
                new Libro(null, "Clean Code", "Robert C. Martin", 2008, "Informatica"),
                new Libro(null, "Learning Spring Boot 3.0", "Greg L. Turnquist", 2022, "Informatica")));

        log.info("Perfil local: se cargaron {} libros de ejemplo en H2", libroRepository.count());
    }
}
