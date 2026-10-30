package com.example.gestorlibros.controller;
import com.example.gestorlibros.model.Libro;
import com.example.gestorlibros.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController                  // Esta clase responde peticiones REST con datos en JSON
@RequestMapping("/api/libros")   // Todas las rutas empiezan con /api/libros
@RequiredArgsConstructor         // Lombok crea el constructor para recibir el servicio
@Tag(name = "Libros", description = "Gestión de libros de la biblioteca") // Título del grupo en Swagger

public class LibroController {
   // El servicio con la lógica (Spring lo entrega automáticamente)
    private final LibroService libroService;

    // GET /api/libros -> lista todos los libros
    @GetMapping
    @Operation(summary = "Listar todos los libros")
    public List<Libro> listar() {
        return libroService.listar();
    }

    // GET /api/libros/buscar?texto=clean -> busca por título o autor
    @GetMapping("/buscar")
    @Operation(summary = "Buscar libros por título o autor")
    public List<Libro> buscar(@RequestParam String texto) {
        return libroService.buscar(texto);
    }

    // GET /api/libros/1 -> trae el libro con id 1
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un libro por su id")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Long id) {
        Libro libro = libroService.buscarPorId(id);

        // Si no existe, responde 404 (No encontrado)
        if (libro == null) {
            return ResponseEntity.notFound().build();
        }
        // Si existe, responde 200 (OK) con el libro
        return ResponseEntity.ok(libro);
    }

    // POST /api/libros -> crea un libro nuevo
    // @Valid revisa las validaciones de Libro (@NotBlank, @Min, etc.)
    @PostMapping
    @Operation(summary = "Agregar un libro nuevo")
    public ResponseEntity<Libro> crear(@Valid @RequestBody Libro libro) {
        Libro nuevo = libroService.guardar(libro);
        // Responde 201 (Creado) con el libro guardado
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // PUT /api/libros/1 -> actualiza el libro con id 1
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un libro existente")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @Valid @RequestBody Libro libro) {
        Libro actualizado = libroService.actualizar(id, libro);

        // Si no existe, responde 404
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/libros/1 -> elimina el libro con id 1
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // Si no existe, responde 404
        if (libroService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        libroService.eliminar(id);
        // Responde 204 (Sin contenido): se eliminó correctamente
        return ResponseEntity.noContent().build();
    }
}


