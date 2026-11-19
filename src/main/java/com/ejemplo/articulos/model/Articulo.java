package com.ejemplo.articulos.model;
import jakarta.persistence.*;
import java.util.*;
import java.time.LocalDateTime;

@Entity
@Table(name="articulo")
public class Articulo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String nombre;
    @Column(length=2000)
    private String descripcion;
    @Column(nullable=false)
    private Double precio;
    @Column(nullable=false, columnDefinition="integer default 0")
    private int stock = 0;
    @Column(length=1000)
    private String imagen;
    @ManyToOne @JoinColumn(name="categoria_id")
    private Categoria categoria;
    public Articulo() {}
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public String getNombre() { return nombre; }
    public void setNombre(String valor) { this.nombre = valor; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String valor) { this.descripcion = valor; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double valor) { this.precio = valor; }
    public int getStock() { return stock; }
    public void setStock(int valor) { this.stock = valor; }
    public String getImagen() { return imagen; }
    public void setImagen(String valor) { this.imagen = valor; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria valor) { this.categoria = valor; }
}
