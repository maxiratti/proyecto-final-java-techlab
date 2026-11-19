package com.ejemplo.articulos.controller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
public class Datos {
 public record Producto(@NotBlank @Size(max=255) String nombre, @Size(max=2000) String descripcion, @NotNull @PositiveOrZero Double precio, @NotNull @PositiveOrZero Integer stock, @NotNull @Positive Long categoriaId, @Size(max=1000) String imagen) {}
 public record CategoriaDatos(@NotBlank @Size(max=255) String nombre) {}
 public record UsuarioDatos(@NotBlank @Size(max=255) String nombre, @NotBlank @Email @Size(max=255) String email) {}
 public record Linea(@NotNull @Positive Long productoId, @NotNull @Positive Integer cantidad) {}
 public record PedidoDatos(@NotNull @Positive Long usuarioId, @NotEmpty @Size(max=100) List<@NotNull @Valid Linea> lineas) {}
 public record Estado(@NotBlank String estado) {}
}
