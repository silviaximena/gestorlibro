package com.example.gestorlibros.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity                 // Esta clase es una tabla en la base de datos
@Table(name = "LIBRO")  // Nombre de la tabla
@Getter                 // Lombok crea todos los getters (getId, getTitulo...)
@Setter                 // Lombok crea todos los setters (setId, setTitulo...)
@NoArgsConstructor      // Constructor vacío: JPA lo necesita obligatoriamente
@AllArgsConstructor     // Constructor con todos los atributos
@Builder  
public class Libro {
   @Id                                                  // Llave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // El id se genera solo (1, 2, 3...)
    private Long id;

    @NotBlank(message = "El título es obligatorio")                       // No puede venir vacío
    @Size(max = 150, message = "El título no puede superar 150 caracteres") // Largo máximo
    @Column(nullable = false, length = 150)                                // Columna obligatoria en la tabla
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 100, message = "El autor no puede superar 100 caracteres")
    @Column(nullable = false, length = 100)
    private String autor;

    @NotNull(message = "El stock es obligatorio")      // No puede venir sin valor
    @Min(value = 0, message = "El stock no puede ser negativo") // Mínimo 0
    private Integer stock;

}
