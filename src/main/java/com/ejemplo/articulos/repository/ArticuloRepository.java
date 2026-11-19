package com.ejemplo.articulos.repository;
import com.ejemplo.articulos.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface ArticuloRepository extends JpaRepository<Articulo,Long> {
List<Articulo> findByNombreContainingIgnoreCase(String nombre);
 boolean existsByCategoriaId(Long id);
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select a from Articulo a where a.id = :id") Optional<Articulo> bloquear(@Param("id") Long id);
}
