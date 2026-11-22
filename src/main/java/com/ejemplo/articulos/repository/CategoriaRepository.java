package com.ejemplo.articulos.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.articulos.model.Categoria;
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
boolean existsByNombreIgnoreCase(String nombre);
}
