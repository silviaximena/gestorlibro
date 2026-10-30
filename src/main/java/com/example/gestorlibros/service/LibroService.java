package com.example.gestorlibros.service;
import com.example.gestorlibros.model.Libro;
import com.example.gestorlibros.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service                  // Le dice a Spring que esta clase tiene la lógica del negocio
@RequiredArgsConstructor  // Lombok crea el constructor para recibir el repositorio
public class LibroService {
   // El repositorio que habla con la base de datos.
    // Es "final" para que Lombok lo incluya en el constructor
    // y Spring lo entregue automáticamente (reemplaza al @Autowired de la guía).
    private final LibroRepository libroRepository;

    // Trae todos los libros
    public List<Libro> listar() {
        return libroRepository.findAll();
    }

    // Busca un libro por su id. Si no existe, devuelve null
    public Libro buscarPorId(Long id) {
        return libroRepository.findById(id).orElse(null);
    }

    // Guarda un libro nuevo (o actualiza uno existente si ya tiene id)
    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    // Actualiza los datos de un libro que ya existe
    public Libro actualizar(Long id, Libro datosNuevos) {
        Libro libroExistente = buscarPorId(id);

        // Si el libro no existe, no hay nada que actualizar
        if (libroExistente == null) {
            return null;
        }

        // Copiamos los datos nuevos sobre el libro existente
        libroExistente.setTitulo(datosNuevos.getTitulo());
        libroExistente.setAutor(datosNuevos.getAutor());
        libroExistente.setStock(datosNuevos.getStock());

        return libroRepository.save(libroExistente);
    }

    // Elimina un libro por su id
    public void eliminar(Long id) {
        libroRepository.deleteById(id);
    }

    // Busca libros cuyo título o autor contenga el texto (desafío adicional 2)
    public List<Libro> buscar(String texto) {
        return libroRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(texto, texto);
    }

}

