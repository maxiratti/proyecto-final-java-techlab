package com.ejemplo.articulos.model;
import jakarta.persistence.*;
import java.util.*;
import java.time.LocalDateTime;

@Entity

public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String nombre;
    @Column(nullable=false, unique=true)
    private String email;
    public Usuario() {}
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public String getNombre() { return nombre; }
    public void setNombre(String valor) { this.nombre = valor; }
    public String getEmail() { return email; }
    public void setEmail(String valor) { this.email = valor; }
}
