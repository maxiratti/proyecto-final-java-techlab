# Guion sugerido — 5 a 7 minutos

Antes de grabar: servidor iniciado, navegador en localhost:8080, usuario seleccionado y un producto con stock conocido. Cerrar ventanas personales. No mostrar contraseñas. Ajustar duración si la profesora indicó otro límite.

## 0:00 — Presentación

“Soy Maximiliano Ratti. Presento TECHLAB, un sistema académico de gestión de productos y pedidos. Partí del ejemplo de la profesora y lo amplié con categorías, stock, usuarios de demostración y una interfaz web. Utiliza Java, Spring Boot, JPA, Maven y MySQL.”

Mostrar el catálogo y el menú.

## 0:30 — Organización

En VS Code, mostrar controller, service, repository, model y resources/static.

“El controlador recibe peticiones HTTP. El servicio aplica las reglas del negocio. Los repositorios acceden a MySQL mediante JPA. Las entidades representan los datos. El frontend usa fetch para comunicarse con la API.”

Mostrar brevemente application.properties, que usa DB_PASSWORD sin revelar su valor.

## 1:15 — Productos y categorías

Crear categoría “Demo video”. Crear “Mouse presentación”, descripción, precio 1000, categoría, stock 5 e imagen opcional. Buscar por nombre y luego por #ID. Editar descripción o precio.

“Los productos se guardan en MySQL. La API permite crear, consultar, actualizar y eliminar. Valida campos obligatorios y rechaza precios o stock negativos.”

Crear otro producto temporal, eliminarlo y mostrar que desaparece.

## 2:30 — Carrito y pedido

Seleccionar un usuario. Agregar dos unidades del producto con stock 5; abrir Carrito y mostrar el total. Crear pedido pendiente.

“Cada pedido pertenece a un usuario y contiene líneas con producto, cantidad y precio de compra. Al crearlo queda pendiente y aún no descuenta stock.”

## 3:15 — Confirmación y alertas

Confirmar el pedido. Volver al catálogo para mostrar que el stock pasó de 5 a 3. Abrir Usuarios y stock y mostrar la alerta.

“La confirmación valida las existencias y descuenta stock dentro de una transacción. Si un producto no alcanza, no se guardan descuentos parciales.”

En Historial, marcar enviado y entregado.

## 4:15 — Validaciones y cancelación

Intentar superar el stock en el carrito para mostrar el mensaje. Crear un segundo pedido de una unidad, confirmar y cancelar; comprobar que vuelve al stock previo.

“Las transiciones están controladas y cancelar un pedido confirmado repone las unidades una sola vez.”

## 5:15 — Usuarios, persistencia y cierre

Mostrar creación de un perfil y filtro del historial. Recargar el navegador para mostrar que pedidos y productos siguen disponibles.

“Los datos de negocio persisten en MySQL. Es una demostración local: la selección de usuario no es un login ni tiene roles de seguridad. El botón Salir limpia la sesión local y el carrito.”

“En este proyecto apliqué POO, colaboración entre clases, colecciones, excepciones, validaciones, API REST y persistencia relacional.”

## Lista de entrega

- Repositorio propio actualizado (conservar referencia al ejemplo docente).
- README/instrucciones para ejecutar y variables necesarias.
- Video accesible para la profesora; comprobar permisos del enlace.
- No subir contraseña, target, archivos .env o datos personales.
- Grabar las operaciones reales; si una falla, corregir antes de afirmar que funciona.
