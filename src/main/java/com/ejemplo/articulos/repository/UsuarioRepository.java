package com.ejemplo.articulos.repository;
import com.ejemplo.articulos.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
boolean existsByEmailIgnoreCase(String email);
}
