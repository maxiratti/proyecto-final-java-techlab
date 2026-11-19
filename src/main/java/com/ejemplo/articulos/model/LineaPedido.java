package com.ejemplo.articulos.model;
import jakarta.persistence.*;
import java.util.*;
import java.time.LocalDateTime;

@Entity

public class LineaPedido {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private Long productoId;
    
    private String nombre;
    
    private double precioUnitario;
    
    private int cantidad;
    public LineaPedido() {}
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long valor) { this.productoId = valor; }
    public String getNombre() { return nombre; }
    public void setNombre(String valor) { this.nombre = valor; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double valor) { this.precioUnitario = valor; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int valor) { this.cantidad = valor; }
    public double getSubtotal() { return precioUnitario * cantidad; }
}
