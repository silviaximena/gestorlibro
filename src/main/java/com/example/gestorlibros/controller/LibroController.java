package com.example.gestorlibros.controller;

import com.example.gestorlibros.model.Libro;
import com.example.gestorlibros.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
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

@RestController                  
@RequestMapping("/api/libros")   
@RequiredArgsConstructor         
@Tag(name = "Libros", description = "Gestión de libros de la biblioteca") 
public class LibroController {

    private final LibroService libroService;

   
    @GetMapping
    @Operation(summary = "Listar todos los libros")
    public List<Libro> listar() {
        return libroService.listar();
    }

    
    @GetMapping("/paginado")
    @Operation(summary = "Listar libros por páginas")
    public PagedModel<Libro> listarPaginado(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanio) {
        return new PagedModel<>(libroService.listarPaginado(pagina, tamanio));
    }

    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar libros por título o autor")
    public List<Libro> buscar(@RequestParam String texto) {
        return libroService.buscar(texto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un libro por su id")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

   
    @PostMapping
    @Operation(summary = "Agregar un libro nuevo")
    public ResponseEntity<Libro> crear(@Valid @RequestBody Libro libro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.guardar(libro));
    }

    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un libro existente")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @Valid @RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.actualizar(id, libro));
    }

   
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}