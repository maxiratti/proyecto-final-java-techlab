package com.ejemplo.articulos.service;
import com.ejemplo.articulos.model.*;
import com.ejemplo.articulos.repository.*;
import com.ejemplo.articulos.controller.Datos.*;
import com.ejemplo.articulos.excepciones.StockInsuficienteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;

@Service
@Transactional
public class TiendaService {
 private final ArticuloRepository articulos; private final CategoriaRepository categorias;
 private final UsuarioRepository usuarios; private final PedidoRepository pedidos;
 public TiendaService(ArticuloRepository a,CategoriaRepository c,UsuarioRepository u,PedidoRepository p) { articulos=a;categorias=c;usuarios=u;pedidos=p; }
 private ResponseStatusException falta(String tipo) { return new ResponseStatusException(HttpStatus.NOT_FOUND,tipo+" no encontrado."); }
 public Articulo producto(Long id) { return articulos.findById(id).orElseThrow(() -> falta("Producto")); }
 public List<Articulo> productos(String nombre) { return nombre==null || nombre.isBlank() ? articulos.findAll() : articulos.findByNombreContainingIgnoreCase(nombre.trim()); }
 public Articulo guardarProducto(Long id,Producto d) {
  if (!Double.isFinite(d.precio())) throw new IllegalArgumentException("Precio inválido.");
  Articulo a=id==null ? new Articulo() : articulos.bloquear(id).orElseThrow(() -> falta("Producto"));
  a.setNombre(d.nombre().trim()); a.setDescripcion(d.descripcion()); a.setPrecio(d.precio()); a.setStock(d.stock());
  if (d.imagen()!=null && !d.imagen().isBlank() && !d.imagen().matches("https?://.+|/img/[A-Za-z0-9_.-]+")) throw new IllegalArgumentException("La imagen debe ser una URL http/https o un recurso local /img/.");
  a.setImagen(d.imagen()); a.setCategoria(categorias.findById(d.categoriaId()).orElseThrow(() -> falta("Categoría")));
  return articulos.save(a);
 }
 public void eliminarProducto(Long id) {
  Articulo a=articulos.bloquear(id).orElseThrow(() -> falta("Producto"));
  for(Pedido p:pedidos.findAll()) if(p.getEstado()==EstadoPedido.PENDIENTE || p.getEstado()==EstadoPedido.CONFIRMADO)
   for(LineaPedido l:p.getLineas()) if(l.getProductoId().equals(id)) throw new IllegalArgumentException("El producto está en un pedido pendiente o confirmado. Finalice o cancele el pedido primero.");
  articulos.delete(a);
 }
 public List<Categoria> categorias() { return categorias.findAll(); }
 public Categoria guardarCategoria(Long id,String nombre) {
  Categoria c=id==null ? new Categoria() : categorias.findById(id).orElseThrow(() -> falta("Categoría"));
  String n=nombre.trim();
  if ((id==null || !n.equalsIgnoreCase(c.getNombre())) && categorias.existsByNombreIgnoreCase(n)) throw new IllegalArgumentException("La categoría ya existe.");
  c.setNombre(n);return categorias.save(c);
 }
 public void eliminarCategoria(Long id) {
  Categoria c=categorias.findById(id).orElseThrow(() -> falta("Categoría"));
  if(articulos.existsByCategoriaId(id)) throw new IllegalArgumentException("La categoría tiene productos asociados.");
  categorias.delete(c);
 }
 public List<Usuario> usuarios() { return usuarios.findAll(); }
 public Usuario guardarUsuario(Long id,String nombre,String email) {
  Usuario u=id==null ? new Usuario() : usuarios.findById(id).orElseThrow(() -> falta("Usuario"));
  String e=email.trim().toLowerCase(Locale.ROOT);
  if ((id==null || !e.equalsIgnoreCase(u.getEmail())) && usuarios.existsByEmailIgnoreCase(e)) throw new IllegalArgumentException("El correo ya está registrado.");
  u.setNombre(nombre.trim());u.setEmail(e);return usuarios.save(u);
 }
 public void eliminarUsuario(Long id) {
  Usuario u=usuarios.findById(id).orElseThrow(() -> falta("Usuario"));
  if(!pedidos.findByUsuarioIdOrderByIdDesc(id).isEmpty()) throw new IllegalArgumentException("El usuario tiene pedidos asociados.");
  usuarios.delete(u);
 }
 // Se agrupan IDs repetidos antes de comprobar stock. La transacción evita descuentos parciales.
 public Pedido crearPedido(PedidoDatos d) {
  Usuario u=usuarios.findById(d.usuarioId()).orElseThrow(() -> falta("Usuario"));
  Map<Long,Long> cantidades=new TreeMap<>();
  for(Linea l:d.lineas()) cantidades.merge(l.productoId(),l.cantidad().longValue(),Long::sum);
  Pedido pedido=new Pedido();pedido.setUsuario(u);
  for(var item:cantidades.entrySet()) {
   Articulo a=articulos.bloquear(item.getKey()).orElseThrow(() -> falta("Producto"));
   if(item.getValue()>a.getStock()) throw new StockInsuficienteException("Stock insuficiente de "+a.getNombre()+". Disponible: "+a.getStock());
   LineaPedido l=new LineaPedido();l.setProductoId(a.getId());l.setNombre(a.getNombre());l.setPrecioUnitario(a.getPrecio());l.setCantidad(item.getValue().intValue());
   pedido.getLineas().add(l);
  }
  if(!Double.isFinite(pedido.getTotal())) throw new IllegalArgumentException("Total demasiado grande.");
  // Pendiente: el stock se descuenta únicamente al pasar a CONFIRMADO.
  return pedidos.save(pedido);
 }
 public List<Pedido> pedidos(Long usuarioId) { return usuarioId==null ? pedidos.findAll() : pedidos.findByUsuarioIdOrderByIdDesc(usuarioId); }
 public Pedido estado(Long id,String valor) {
  Pedido p=pedidos.bloquear(id).orElseThrow(() -> falta("Pedido"));
  EstadoPedido nuevo;
  try { nuevo=EstadoPedido.valueOf(valor.toUpperCase(Locale.ROOT)); } catch(IllegalArgumentException e) { throw new IllegalArgumentException("Estado inválido."); }
  EstadoPedido anterior=p.getEstado();
  if(nuevo==anterior) return p;
  boolean permitido=(anterior==EstadoPedido.PENDIENTE && (nuevo==EstadoPedido.CONFIRMADO || nuevo==EstadoPedido.CANCELADO))
   || (anterior==EstadoPedido.CONFIRMADO && (nuevo==EstadoPedido.ENVIADO || nuevo==EstadoPedido.CANCELADO))
   || (anterior==EstadoPedido.ENVIADO && nuevo==EstadoPedido.ENTREGADO);
  if(!permitido) throw new IllegalArgumentException("Transición de estado no permitida.");
  List<LineaPedido> ordenadas=new ArrayList<>(p.getLineas());ordenadas.sort(Comparator.comparing(LineaPedido::getProductoId));
  if(nuevo==EstadoPedido.CONFIRMADO || (nuevo==EstadoPedido.CANCELADO && anterior==EstadoPedido.CONFIRMADO)) {
   for(LineaPedido l:ordenadas) {
    Articulo a=articulos.bloquear(l.getProductoId()).orElseThrow(() -> new IllegalArgumentException("El producto "+l.getNombre()+" fue eliminado. No se puede ajustar su stock."));
    if(nuevo==EstadoPedido.CONFIRMADO) {
     if(l.getCantidad()>a.getStock()) throw new StockInsuficienteException("Stock insuficiente de "+a.getNombre());
     a.setStock(a.getStock()-l.getCantidad());
    } else {
     long repuesto=(long)a.getStock()+l.getCantidad();
     if(repuesto>Integer.MAX_VALUE) throw new IllegalArgumentException("El stock resultante supera el máximo permitido.");
     a.setStock((int)repuesto);
    }
   }
  }
  p.setEstado(nuevo);return p;
 }
}
