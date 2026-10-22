package com.biblioteca.gestionbiblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * Entidad JPA que representa un libro de la biblioteca.
 *
 * Atributos: ID, titulo, autor, anio de publicacion y genero.
 * Se mapea a la tabla LIBRO de la base de datos Oracle (ver scripts/01_crear_tabla_libro.sql).
 */
@Entity
@Table(name = "LIBRO")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "libro_seq_gen")
    @SequenceGenerator(name = "libro_seq_gen", sequenceName = "LIBRO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITULO", nullable = false, length = 200)
    private String titulo;

    @Column(name = "AUTOR", nullable = false, length = 150)
    private String autor;

    @Column(name = "ANIO_PUBLICACION", nullable = false)
    private Integer anioPublicacion;

    @Column(name = "GENERO", nullable = false, length = 100)
    private String genero;

    protected Libro() {
        // Constructor requerido por JPA.
    }

    public Libro(Long id, String titulo, String autor, Integer anioPublicacion, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.genero = genero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Libro otro)) {
            return false;
        }
        return id != null && id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Libro{id=" + id
                + ", titulo='" + titulo + '\''
                + ", autor='" + autor + '\''
                + ", anioPublicacion=" + anioPublicacion
                + ", genero='" + genero + '\'' + '}';
    }
}
