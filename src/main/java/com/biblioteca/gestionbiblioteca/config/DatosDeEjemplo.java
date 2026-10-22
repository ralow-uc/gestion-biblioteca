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
 * Carga un catalogo de obras de autores chilenos al iniciar con el perfil "local"
 * (H2 en memoria),
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
                new Libro(null, "Desolación", "Gabriela Mistral", 1922, "Poesía"),
                new Libro(null, "Veinte poemas de amor y una canción desesperada", "Pablo Neruda", 1924, "Poesía"),
                new Libro(null, "Papelucho", "Marcela Paz", 1947, "Literatura infantil"),
                new Libro(null, "Hijo de ladrón", "Manuel Rojas", 1951, "Novela"),
                new Libro(null, "Poemas y antipoemas", "Nicanor Parra", 1954, "Antipoesía"),
                new Libro(null, "El obsceno pájaro de la noche", "José Donoso", 1970, "Novela"),
                new Libro(null, "La casa de los espíritus", "Isabel Allende", 1982, "Realismo mágico"),
                new Libro(null, "Ardiente paciencia", "Antonio Skármeta", 1985, "Novela"),
                new Libro(null, "Un viejo que leía novelas de amor", "Luis Sepúlveda", 1989, "Novela"),
                new Libro(null, "Los detectives salvajes", "Roberto Bolaño", 1998, "Novela"),
                new Libro(null, "Formas de volver a casa", "Alejandro Zambra", 2011, "Novela")));

        log.info("Perfil local: se cargaron {} libros de ejemplo en H2", libroRepository.count());
    }
}
