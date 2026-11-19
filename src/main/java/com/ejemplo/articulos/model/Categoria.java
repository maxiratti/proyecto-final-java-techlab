package com.ejemplo.articulos.model;
import jakarta.persistence.*;
import java.util.*;
import java.time.LocalDateTime;

@Entity

public class Categoria {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
    private String nombre;
    public Categoria() {}
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public String getNombre() { return nombre; }
    public void setNombre(String valor) { this.nombre = valor; }
}
