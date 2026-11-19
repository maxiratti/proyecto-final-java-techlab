package com.ejemplo.articulos.config;
import org.springframework.context.annotation.*;
import org.springframework.boot.CommandLineRunner;
import com.ejemplo.articulos.model.*;
import com.ejemplo.articulos.repository.*;
@Configuration
public class DatosIniciales {
 @Bean CommandLineRunner cargar(CategoriaRepository c,UsuarioRepository u,ArticuloRepository a) {
  return args -> {
   if(c.count()==0) for(String n:new String[]{"Computación","Accesorios","Audio"}) {Categoria x=new Categoria();x.setNombre(n);c.save(x);}
   if(u.count()==0) {Usuario x=new Usuario();x.setNombre("Maxi Ratti");x.setEmail("maxi@example.com");u.save(x);}
   if(a.count()==0) {
    String[] nombres={"Notebook Pro 14","Mouse inalámbrico","Auriculares Studio","Teclado compacto"};
    double[] precios={850000,18000,45000,32000};int[] stocks={6,12,3,8};
    var cats=c.findAll();
    for(int i=0;i<nombres.length;i++) {Articulo p=new Articulo();p.setNombre(nombres[i]);p.setPrecio(precios[i]);p.setStock(stocks[i]);p.setDescripcion("Tecnología para estudiar, trabajar y crear.");p.setCategoria(cats.get(i%cats.size()));p.setImagen("/img/producto.svg");a.save(p);}
   }
  };
 }
}
