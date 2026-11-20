# TECHLAB — Proyecto final de Backend Java

Versión ampliada del ejemplo de la profesora Gisele González, para la entrega de Maximiliano Ratti. Mantiene Java 17 (compatible con JDK 21), Spring Boot 3.2.5, Maven, JPA y MySQL; agrega frontend HTML/CSS/JS servido por la misma aplicación.

## 1. Instalar sin mezclar versiones

1. Si el servidor anterior está funcionando, detenerlo con Ctrl+C. Si pregunta por el trabajo por lotes, responder S.
2. Descomprimir este ZIP en `D:\BackEnd-JAVA`. Quedará la carpeta `proyecto-final-techlab`.
3. Mantener el proyecto anterior como respaldo. No copiar la nueva carpeta src encima de la anterior: algunas clases fueron reemplazadas.
4. Abrir `D:\BackEnd-JAVA\proyecto-final-techlab` en VS Code.
5. MySQL80 debe estar iniciado y la base `articulos_db` ya debe existir, como se configuró previamente. No se borra la base ni sus datos.

## 2. Ejecutar

En la terminal PowerShell de VS Code:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\INICIAR.ps1
```

El permiso se aplica solamente al proceso que ejecuta este script. Se pide la contraseña de MySQL sin mostrarla. No escribirla en application.properties ni subirla a GitHub.

Esperar `Started ArticuloApiApplication` y abrir:

http://localhost:8080

No abrir el HTML con doble clic ni usar Live Server: Spring Boot sirve el frontend y la API desde el mismo origen.

La primera ejecución descarga dependencias. Si aparece `Access denied`, la contraseña o el usuario de MySQL son incorrectos. Si el puerto 8080 está ocupado, detener el servidor anterior. El script asume Maven en `D:\Herramientas\apache-maven-3.10.1\bin\mvn.cmd`; se puede editar esa ruta.

Si se prefiere ejecutar manualmente:

```powershell
$claveTechlab = Read-Host 'Contraseña de MySQL root' -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $claveTechlab).Password
& 'D:\Herramientas\apache-maven-3.10.1\bin\mvn.cmd' spring-boot:run
```

## 3. Primera prueba

Se crean categorías y un usuario de demostración si no existen. Si el catálogo está vacío, se agregan cuatro productos de muestra. Si ya había un producto de prueba, se conserva; los nuevos campos pueden estar vacíos y su stock será 0: editarlo desde el catálogo y asignarle categoría, descripción, imagen y stock.

1. Seleccionar el usuario Maxi Ratti en la barra lateral.
2. En Productos, agregar o editar un producto: precio 1000, stock 5, categoría existente.
3. Buscarlo por nombre o por `#ID`.
4. Agregar dos unidades al carrito.
5. Abrir Carrito y crear el pedido pendiente. El stock todavía es 5.
6. En Historial, confirmar el pedido. El stock baja a 3 y aparece la alerta de stock mínimo.
7. Marcar enviado y entregado; mostrar el total y las líneas del historial.
8. Para demostrar cancelación, crear otro pedido, confirmarlo y cancelarlo antes de enviarlo. Se repone el stock.
9. Para probar falta de stock, intentar pedir más de lo disponible. La interfaz y la API lo rechazan.
10. Crear un producto temporal y eliminarlo para demostrar DELETE.

Los cambios de estado permitidos son PENDIENTE → CONFIRMADO → ENVIADO → ENTREGADO, o CANCELADO desde PENDIENTE/CONFIRMADO. Confirmar de nuevo el mismo estado no duplica el descuento. Un pedido enviado o entregado no puede cancelarse desde esta entrega.

## 4. Funciones incluidas

- Productos: alta, listado, detalle por ID, búsqueda por nombre, edición y eliminación; nombre, descripción, precio, categoría, URL de imagen y stock.
- Categorías: alta, listado, edición y eliminación; se bloquea eliminar una categoría en uso.
- Carrito: agregar, cambiar cantidades, quitar y calcular total.
- Pedidos: usuario, fecha, líneas con precio y nombre de compra, total y estado persistentes.
- Confirmación transaccional: comprueba todo el stock y lo descuenta; si falla alguna línea, se revierten los descuentos.
- Cancelación de confirmado: repone stock una sola vez.
- Usuarios: alta, listado, edición y eliminación de perfiles de demostración. No se eliminan usuarios con pedidos.
- Historial por usuario y vista de administración de todos los pedidos.
- Alertas de stock <= 3 unidades.
- Salir: limpia la selección de usuario y el carrito de esta sesión local.
- Validación de datos y respuestas HTTP 400, 404 y 409 con mensajes.
- Los productos en pedidos pendientes/confirmados no se eliminan; sus líneas conservan nombre/precio aunque el catálogo cambie después.

## 5. Alcance del sistema

Aplicación académica de uso local, sin autenticación ni autorización por roles. La selección de un perfil no verifica identidad; la pantalla lo indica. No se procesan pagos ni envíos reales. El carrito permanece en memoria del navegador y se pierde al recargar; los pedidos, productos, usuarios y categorías se guardan en MySQL. La selección de usuario se conserva localmente hasta Salir. El servidor escucha en 127.0.0.1.

El stock se descuenta al CONFIRMAR, como pide el requisito general. El enunciado contiene un ejemplo que mezcla confirmar y pendiente; aquí se separan los dos momentos explícitamente. El precio de la línea se toma al crear el pedido y queda fijado. No hay reservas de stock mientras está pendiente: se vuelve a validar al confirmar.

Se conservó `Long` para IDs y `Double/double` para precios del ejemplo original; cantidades y stock son int. Para producción convendría BigDecimal, autenticación, roles y migraciones versionadas. `ddl-auto=update` se usa por el curso.

Las imágenes pueden ser URLs http/https; si fallan, se muestra la ilustración local. No se necesita conexión para los estilos, scripts o imagen predeterminada.

## 6. Estructura del proyecto

`controller`: endpoints REST, DTO de entrada y manejo de errores.
`service/TiendaService`: reglas, validaciones, transacciones y stock.
`repository`: interfaces JpaRepository y bloqueos de registros.
`model`: entidades Articulo, Categoria, Usuario, Pedido, LineaPedido y enum EstadoPedido.
`config`: datos iniciales.
`resources/static`: index.html, css/app.css, js/app.js e imagen SVG local.
`src/test`: pruebas de integración con H2 en modo MySQL (no toca la base de la netbook).

Consultar `API_Y_PRUEBAS.md` para los endpoints y las pruebas.
