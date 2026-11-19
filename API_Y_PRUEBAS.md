# API REST y pruebas

## Endpoints

| Método | Ruta | Acción |
| --- | --- | --- |
| GET | /api/productos | Lista; filtro opcional ?nombre=mouse |
| GET | /api/productos/{id} | Detalle |
| POST | /api/productos | Crear |
| PUT | /api/productos/{id} | Editar |
| DELETE | /api/productos/{id} | Eliminar |
| GET/POST | /api/categorias | Listar/crear |
| PUT/DELETE | /api/categorias/{id} | Editar/eliminar |
| GET/POST | /api/usuarios | Listar/crear |
| PUT/DELETE | /api/usuarios/{id} | Editar/eliminar |
| GET/POST | /api/pedidos | Listar/crear; GET acepta ?usuarioId=1 |
| GET | /api/usuarios/{id}/pedidos | Historial por usuario |
| PATCH | /api/pedidos/{id}/estado | Cambiar estado |
| GET | /api/stock/bajo | Stock <= 3 |

`/api/articulos` es alias compatible del CRUD de productos. El POST anterior de solo nombre/precio debe actualizarse: ahora requiere stock y categoría.

## JSON de producto

```json
{"nombre":"Mouse","descripcion":"Mouse de prueba","precio":1000,"stock":5,"categoriaId":1,"imagen":""}
```

Usar un categoriaId existente. La respuesta incluye `categoria` como objeto, no categoriaId.

## JSON de pedido

```json
{"usuarioId":1,"lineas":[{"productoId":1,"cantidad":2}]}
```

Usar IDs existentes. Los IDs repetidos se agrupan antes de validar. La respuesta incluye estado PENDIENTE y total calculado en el servidor.

## JSON de cambio de estado

```json
{"estado":"CONFIRMADO"}
```

El frontend y backend están en el mismo origen; no hace falta habilitar CORS. Los endpoints no tienen autenticación: uso local académico.

## Ejecutar pruebas automáticas

En PowerShell desde la carpeta del proyecto:

```powershell
& 'D:\Herramientas\apache-maven-3.10.1\bin\mvn.cmd' test
```

Las pruebas usan H2 en modo MySQL; no acceden a articulos_db ni necesitan su contraseña. Verifican creación pendiente sin descuento, confirmación, idempotencia, cancelación con reposición, estados inválidos, entrega, rollback cuando falla una línea, IDs repetidos, historial con precio fijo, entradas HTTP inválidas, 404 y frontend disponible.

La compilación y las pruebas en H2 no sustituyen la comprobación final contra MySQL de la netbook. Revisar allí alta, edición, pedido, estados y persistencia.
