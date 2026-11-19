package com.ejemplo.articulos.repository;
import com.ejemplo.articulos.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
boolean existsByNombreIgnoreCase(String nombre);
}
