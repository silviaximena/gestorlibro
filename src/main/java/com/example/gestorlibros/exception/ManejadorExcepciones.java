package com.example.gestorlibros.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
public class ManejadorExcepciones {

    
    @ExceptionHandler(LibroNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(LibroNoEncontradoException ex) {
        return crearRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

   
   
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        return crearRespuesta(HttpStatus.BAD_REQUEST, "Hay datos inválidos", errores);
    }

   
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        return crearRespuesta(HttpStatus.BAD_REQUEST, "El JSON está mal escrito. Revisa llaves, comillas y comas", null);
    }

   
    private ResponseEntity<Map<String, Object>> crearRespuesta(HttpStatus estado, String mensaje, Object detalles) {
        Map<String, Object> cuerpo = new LinkedHashMap<>(); // LinkedHashMap mantiene el orden de los campos
        cuerpo.put("fecha", LocalDateTime.now().toString());
        cuerpo.put("estado", estado.value());
        cuerpo.put("error", estado.getReasonPhrase());
        cuerpo.put("mensaje", mensaje);
        if (detalles != null) {
            cuerpo.put("detalles", detalles);
        }
        return ResponseEntity.status(estado).body(cuerpo);
    }
}