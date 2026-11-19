package com.ejemplo.articulos.controller;
import com.ejemplo.articulos.model.*;
import com.ejemplo.articulos.service.TiendaService;
import com.ejemplo.articulos.controller.Datos.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TiendaController {
 private final TiendaService tienda;
 public TiendaController(TiendaService t) {tienda=t;}
 @GetMapping({"/articulos","/productos"}) public List<Articulo> productos(@RequestParam(required=false) String nombre) {return tienda.productos(nombre);}
 @GetMapping({"/articulos/{id}","/productos/{id}"}) public Articulo producto(@PathVariable Long id) {return tienda.producto(id);}
 @PostMapping({"/articulos","/productos"}) @ResponseStatus(HttpStatus.CREATED) public Articulo crear(@Valid @RequestBody Producto d) {return tienda.guardarProducto(null,d);}
 @PutMapping({"/articulos/{id}","/productos/{id}"}) public Articulo editar(@PathVariable Long id,@Valid @RequestBody Producto d) {return tienda.guardarProducto(id,d);}
 @DeleteMapping({"/articulos/{id}","/productos/{id}"}) @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) {tienda.eliminarProducto(id);}
 @GetMapping("/categorias") public List<Categoria> categorias() {return tienda.categorias();}
 @PostMapping("/categorias") @ResponseStatus(HttpStatus.CREATED) public Categoria categoria(@Valid @RequestBody CategoriaDatos d) {return tienda.guardarCategoria(null,d.nombre());}
 @PutMapping("/categorias/{id}") public Categoria categoriaEditar(@PathVariable Long id,@Valid @RequestBody CategoriaDatos d) {return tienda.guardarCategoria(id,d.nombre());}
 @DeleteMapping("/categorias/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void categoriaEliminar(@PathVariable Long id) {tienda.eliminarCategoria(id);}
 @GetMapping("/usuarios") public List<Usuario> usuarios() {return tienda.usuarios();}
 @PostMapping("/usuarios") @ResponseStatus(HttpStatus.CREATED) public Usuario usuario(@Valid @RequestBody UsuarioDatos d) {return tienda.guardarUsuario(null,d.nombre(),d.email());}
 @PutMapping("/usuarios/{id}") public Usuario usuarioEditar(@PathVariable Long id,@Valid @RequestBody UsuarioDatos d) {return tienda.guardarUsuario(id,d.nombre(),d.email());}
 @DeleteMapping("/usuarios/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void usuarioEliminar(@PathVariable Long id) {tienda.eliminarUsuario(id);}
 @PostMapping("/pedidos") @ResponseStatus(HttpStatus.CREATED) public Pedido pedido(@Valid @RequestBody PedidoDatos d) {return tienda.crearPedido(d);}
 @GetMapping("/pedidos") public List<Pedido> pedidos(@RequestParam(required=false) Long usuarioId) {return tienda.pedidos(usuarioId);}
 @GetMapping("/usuarios/{id}/pedidos") public List<Pedido> historial(@PathVariable Long id) {return tienda.pedidos(id);}
 @PatchMapping("/pedidos/{id}/estado") public Pedido estado(@PathVariable Long id,@Valid @RequestBody Estado d) {return tienda.estado(id,d.estado());}
 @GetMapping("/stock/bajo") public List<Articulo> bajo() {return tienda.productos(null).stream().filter(p->p.getStock()<=3).toList();}
}
