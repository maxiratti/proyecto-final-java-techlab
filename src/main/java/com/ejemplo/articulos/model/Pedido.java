package com.ejemplo.articulos.model;
import jakarta.persistence.*;
import java.util.*;
import java.time.LocalDateTime;

@Entity

public class Pedido {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional=false)
    private Usuario usuario;
    
    private LocalDateTime fecha = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true) @JoinColumn(name="pedido_id")
    private List<LineaPedido> lineas = new ArrayList<>();
    public Pedido() {}
    public Long getId() { return id; }
    public void setId(Long valor) { this.id = valor; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario valor) { this.usuario = valor; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime valor) { this.fecha = valor; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido valor) { this.estado = valor; }
    public List<LineaPedido> getLineas() { return lineas; }
    public void setLineas(List<LineaPedido> valor) { this.lineas = valor; }
    public double getTotal() { return lineas.stream().mapToDouble(LineaPedido::getSubtotal).sum(); }
}
