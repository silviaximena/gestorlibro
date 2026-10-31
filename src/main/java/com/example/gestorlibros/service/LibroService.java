package com.example.gestorlibros.service;

import com.example.gestorlibros.exception.LibroNoEncontradoException;
import com.example.gestorlibros.model.Libro;
import com.example.gestorlibros.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service                  
@RequiredArgsConstructor 
public class LibroService {

  
    private final LibroRepository libroRepository;

   
    public List<Libro> listar() {
        return libroRepository.findAll();
    }


    public Page<Libro> listarPaginado(int pagina, int tamanio) {
        return libroRepository.findAll(PageRequest.of(pagina, tamanio, Sort.by("id")));
    }

    
    public Libro buscarPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new LibroNoEncontradoException(id));
    }

    
    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

   
    public Libro actualizar(Long id, Libro datosNuevos) {
        Libro libroExistente = buscarPorId(id);

        libroExistente.setTitulo(datosNuevos.getTitulo());
        libroExistente.setAutor(datosNuevos.getAutor());
        libroExistente.setStock(datosNuevos.getStock());

        return libroRepository.save(libroExistente);
    }


    public void eliminar(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new LibroNoEncontradoException(id);
        }
        libroRepository.deleteById(id);
    }

  
    public List<Libro> buscar(String texto) {
        return libroRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(texto, texto);
    }
}