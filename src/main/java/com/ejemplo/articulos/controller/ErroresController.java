package com.ejemplo.articulos.controller;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;
import com.ejemplo.articulos.excepciones.StockInsuficienteException;
@RestControllerAdvice
public class ErroresController {
 @ExceptionHandler({IllegalArgumentException.class, StockInsuficienteException.class, HttpMessageNotReadableException.class})
 public ResponseEntity<?> entrada(Exception e) { return ResponseEntity.badRequest().body(Map.of("mensaje", e instanceof HttpMessageNotReadableException ? "JSON inválido o valor incorrecto." : e.getMessage())); }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<?> validacion(MethodArgumentNotValidException e) { return ResponseEntity.badRequest().body(Map.of("mensaje", e.getBindingResult().getFieldErrors().stream().map(x -> x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Datos inválidos"))); }
 @ExceptionHandler(ResponseStatusException.class)
 public ResponseEntity<?> noExiste(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(Map.of("mensaje", e.getReason()==null ? "Recurso no encontrado" : e.getReason())); }
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<?> conflicto(DataIntegrityViolationException e) { return ResponseEntity.status(409).body(Map.of("mensaje","El registro está relacionado o sus datos ya existen.")); }
}
