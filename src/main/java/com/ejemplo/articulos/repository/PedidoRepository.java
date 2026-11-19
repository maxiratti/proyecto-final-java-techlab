package com.ejemplo.articulos.repository;
import com.ejemplo.articulos.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface PedidoRepository extends JpaRepository<Pedido,Long> {
List<Pedido> findByUsuarioIdOrderByIdDesc(Long id);
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select p from Pedido p where p.id = :id") Optional<Pedido> bloquear(@Param("id") Long id);
}
