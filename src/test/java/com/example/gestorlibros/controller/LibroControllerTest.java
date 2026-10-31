package com.example.gestorlibros.controller;

import com.example.gestorlibros.exception.LibroNoEncontradoException;
import com.example.gestorlibros.model.Libro;
import com.example.gestorlibros.service.LibroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibroController.class)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibroService libroService;

    private final Libro libro = new Libro(1L, "Clean Code", "Robert C. Martin", 4);

    @Test
    void listarDevuelve200ConLosLibros() throws Exception {
        when(libroService.listar()).thenReturn(List.of(libro));

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Clean Code"));
    }

    @Test
    void paginadoDevuelveLaPagina() throws Exception {
        when(libroService.listarPaginado(0, 5))
                .thenReturn(new PageImpl<>(List.of(libro), PageRequest.of(0, 5), 1));

        mockMvc.perform(get("/api/libros/paginado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo").value("Clean Code"))
                .andExpect(jsonPath("$.page.totalElements").value(1));
    }

    @Test
    void buscarDevuelveLosResultados() throws Exception {
        when(libroService.buscar("clean")).thenReturn(List.of(libro));

        mockMvc.perform(get("/api/libros/buscar").param("texto", "clean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].autor").value("Robert C. Martin"));
    }

    @Test
    void buscarPorIdDevuelve200SiExiste() throws Exception {
        when(libroService.buscarPorId(1L)).thenReturn(libro);

        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Clean Code"));
    }

    @Test
    void buscarPorIdDevuelve404SiNoExiste() throws Exception {
        when(libroService.buscarPorId(99L)).thenThrow(new LibroNoEncontradoException(99L));

        mockMvc.perform(get("/api/libros/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe un libro con id 99"));
    }

    @Test
    void crearDevuelve201ConDatosValidos() throws Exception {
        when(libroService.guardar(any(Libro.class))).thenReturn(libro);

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Clean Code\",\"autor\":\"Robert C. Martin\",\"stock\":4}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void crearDevuelve400SiElTituloEstaVacio() throws Exception {
        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"\",\"autor\":\"Neruda\",\"stock\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.titulo").value("El título es obligatorio"));

        verify(libroService, never()).guardar(any(Libro.class));
    }

    @Test
    void crearDevuelve400SiElJsonEstaMalEscrito() throws Exception {
        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{{\"titulo\":\"X\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El JSON está mal escrito. Revisa llaves, comillas y comas"));
    }

    @Test
    void actualizarDevuelve200() throws Exception {
        when(libroService.actualizar(eq(1L), any(Libro.class))).thenReturn(libro);

        mockMvc.perform(put("/api/libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Clean Code\",\"autor\":\"Robert C. Martin\",\"stock\":4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Clean Code"));
    }

    @Test
    void eliminarDevuelve204() throws Exception {
        mockMvc.perform(delete("/api/libros/1"))
                .andExpect(status().isNoContent());

        verify(libroService).eliminar(1L);
    }

    @Test
    void eliminarDevuelve404SiNoExiste() throws Exception {
        doThrow(new LibroNoEncontradoException(99L)).when(libroService).eliminar(99L);

        mockMvc.perform(delete("/api/libros/99"))
                .andExpect(status().isNotFound());
    }
}