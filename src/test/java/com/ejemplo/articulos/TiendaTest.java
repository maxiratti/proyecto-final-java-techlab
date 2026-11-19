package com.ejemplo.articulos;
import com.ejemplo.articulos.service.TiendaService;
import com.ejemplo.articulos.model.*;
import com.ejemplo.articulos.repository.*;
import com.ejemplo.articulos.controller.Datos.*;
import com.ejemplo.articulos.excepciones.StockInsuficienteException;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:tienda;MODE=MySQL;DB_CLOSE_DELAY=-1", "spring.datasource.username=sa","spring.datasource.password=", "spring.datasource.driver-class-name=org.h2.Driver"})
@AutoConfigureMockMvc
class TiendaTest {
 @Autowired TiendaService tienda; @Autowired ArticuloRepository articulos; @Autowired PedidoRepository pedidos; @Autowired MockMvc mvc;
 private Articulo producto(int stock) {return tienda.guardarProducto(null,new Producto("Prueba "+UUID.randomUUID(),"Descripción",10.0,stock,tienda.categorias().get(0).getId(),""));}
 private Pedido crear(Articulo a,int n) {return tienda.crearPedido(new PedidoDatos(tienda.usuarios().get(0).getId(),List.of(new Linea(a.getId(),n))));}
 @Test void cicloStockYEstados(){Articulo a=producto(10);Pedido p=crear(a,3);assertEquals(10,tienda.producto(a.getId()).getStock());assertEquals(30,p.getTotal());tienda.estado(p.getId(),"CONFIRMADO");assertEquals(7,tienda.producto(a.getId()).getStock());tienda.estado(p.getId(),"CONFIRMADO");assertEquals(7,tienda.producto(a.getId()).getStock());tienda.estado(p.getId(),"CANCELADO");assertEquals(10,tienda.producto(a.getId()).getStock());assertThrows(IllegalArgumentException.class,()->tienda.estado(p.getId(),"ENVIADO"));}
 @Test void falloNoDescuentaParcialmente(){Articulo a=producto(10),b=producto(3);Pedido p=tienda.crearPedido(new PedidoDatos(tienda.usuarios().get(0).getId(),List.of(new Linea(a.getId(),2),new Linea(b.getId(),3))));b.setStock(1);articulos.save(b);assertThrows(StockInsuficienteException.class,()->tienda.estado(p.getId(),"CONFIRMADO"));assertEquals(10,tienda.producto(a.getId()).getStock());assertEquals(EstadoPedido.PENDIENTE,tienda.pedidos(null).stream().filter(x->x.getId().equals(p.getId())).findFirst().get().getEstado());}
 @Test void idsRepetidosEHistorial(){Articulo a=producto(5);assertThrows(StockInsuficienteException.class,()->tienda.crearPedido(new PedidoDatos(tienda.usuarios().get(0).getId(),List.of(new Linea(a.getId(),3),new Linea(a.getId(),3)))));Pedido p=crear(a,2);String original=a.getNombre();a.setNombre("Otro");a.setPrecio(99.0);articulos.save(a);Pedido obtenido=tienda.pedidos(null).stream().filter(x->x.getId().equals(p.getId())).findFirst().get();assertEquals(original,obtenido.getLineas().get(0).getNombre());assertEquals(20,obtenido.getTotal());}
 @Test void entregaYCancelacionInvalida(){Articulo a=producto(5);Pedido p=crear(a,1);tienda.estado(p.getId(),"CONFIRMADO");tienda.estado(p.getId(),"ENVIADO");tienda.estado(p.getId(),"ENTREGADO");assertThrows(IllegalArgumentException.class,()->tienda.estado(p.getId(),"CANCELADO"));assertEquals(4,tienda.producto(a.getId()).getStock());}
 @Test void validacionesHttp() throws Exception {mvc.perform(post("/api/productos").contentType("application/json").content("{\"nombre\":\"x\",\"precio\":1,\"stock\":-1,\"categoriaId\":1}")).andExpect(status().isBadRequest());mvc.perform(get("/api/productos/999999")).andExpect(status().isNotFound());mvc.perform(post("/api/pedidos").contentType("application/json").content("{\"usuarioId\":1,\"lineas\":[]}")).andExpect(status().isBadRequest());mvc.perform(get("/")).andExpect(status().isOk());}
}
