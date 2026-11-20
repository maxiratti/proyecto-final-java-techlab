# Verificación realizada

- Compilación de las fuentes Java con nivel de lenguaje 17 en JDK 21.
- Cinco pruebas de integración JUnit: 0 fallos, 0 errores; base H2 en modo MySQL.
- Prueba del frontend en un DOM simulado contra un servidor Spring Boot real: catálogo, selección de usuario, carrito, creación de pedido, confirmación, cancelación, actualización de stock, alta de producto, búsqueda, eliminación y salida de sesión local.
- Comprobación de sintaxis JavaScript con Node.

No se ejecutó esta versión contra el servidor MySQL de la netbook desde aquí. Ese último control debe hacerse localmente siguiendo README.md antes de grabar. No se pudo completar la inspección visual con Chromium por una restricción del entorno de prueba; revisar la presentación en Chrome local.
