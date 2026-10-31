package com.example.gestorlibros.repository;
import com.example.gestorlibros.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository 
public interface LibroRepository  extends JpaRepository<Libro, Long>{
  List<Libro> findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(String titulo, String autor);


}
