package com.ejemplo.articulos.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.articulos.model.Usuario;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
boolean existsByEmailIgnoreCase(String email);
}
