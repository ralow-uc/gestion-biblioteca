package com.biblioteca.gestionbiblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio de gestion de libros de la biblioteca.
 *
 * Expone la API REST de gestion de libros.
 */
@SpringBootApplication
public class GestionBibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionBibliotecaApplication.class, args);
    }
}
