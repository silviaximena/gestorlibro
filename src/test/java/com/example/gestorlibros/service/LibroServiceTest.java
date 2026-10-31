package com.example.gestorlibros.service;

import com.example.gestorlibros.exception.LibroNoEncontradoException;
import com.example.gestorlibros.model.Libro;
import com.example.gestorlibros.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    private Libro libro;

    @BeforeEach
    void setUp() {
        libro = new Libro(1L, "Clean Code", "Robert C. Martin", 4);
    }

    @Test
    void listarDevuelveTodosLosLibros() {
        when(libroRepository.findAll()).thenReturn(List.of(libro));

        List<Libro> resultado = libroService.listar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Clean Code");
    }

    @Test
    void listarPaginadoDevuelveLaPagina() {
        Page<Libro> pagina = new PageImpl<>(List.of(libro));
        when(libroRepository.findAll(any(Pageable.class))).thenReturn(pagina);

        Page<Libro> resultado = libroService.listarPaginado(0, 5);

        assertThat(resultado.getTotalElements()).isEqualTo(1);
    }

    @Test
    void buscarPorIdDevuelveElLibroSiExiste() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        Libro resultado = libroService.buscarPorId(1L);

        assertThat(resultado.getAutor()).isEqualTo("Robert C. Martin");
    }

    @Test
    void buscarPorIdLanzaExcepcionSiNoExiste() {
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> libroService.buscarPorId(99L))
                .isInstanceOf(LibroNoEncontradoException.class)
                .hasMessage("No existe un libro con id 99");
    }

    @Test
    void guardarLlamaAlRepositorio() {
        when(libroRepository.save(libro)).thenReturn(libro);

        Libro resultado = libroService.guardar(libro);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(libroRepository).save(libro);
    }

    @Test
    void actualizarCambiaLosDatos() {
        Libro datosNuevos = new Libro(null, "Clean Code 2", "Robert C. Martin", 10);
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroRepository.save(any(Libro.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Libro resultado = libroService.actualizar(1L, datosNuevos);

        assertThat(resultado.getTitulo()).isEqualTo("Clean Code 2");
        assertThat(resultado.getStock()).isEqualTo(10);
    }

    @Test
    void actualizarLanzaExcepcionSiNoExiste() {
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> libroService.actualizar(99L, libro))
                .isInstanceOf(LibroNoEncontradoException.class);
    }

    @Test
    void eliminarBorraElLibroSiExiste() {
        when(libroRepository.existsById(1L)).thenReturn(true);

        libroService.eliminar(1L);

        verify(libroRepository).deleteById(1L);
    }

    @Test
    void eliminarLanzaExcepcionSiNoExiste() {
        when(libroRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> libroService.eliminar(99L))
                .isInstanceOf(LibroNoEncontradoException.class);
        verify(libroRepository, never()).deleteById(anyLong());
    }

    @Test
    void buscarUsaElTextoEnTituloYAutor() {
        when(libroRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase("clean", "clean"))
                .thenReturn(List.of(libro));

        List<Libro> resultado = libroService.buscar("clean");

        assertThat(resultado).hasSize(1);
    }
}