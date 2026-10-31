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

@Entity                 
@Table(name = "LIBRO") 
@Getter                
@Setter                
@NoArgsConstructor      
@AllArgsConstructor     
@Builder  
public class Libro {
   @Id                                              
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;

    @NotBlank(message = "El título es obligatorio")                      
    @Size(max = 150, message = "El título no puede superar 150 caracteres") 
    @Column(nullable = false, length = 150)                                
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 100, message = "El autor no puede superar 100 caracteres")
    @Column(nullable = false, length = 100)
    private String autor;

    @NotNull(message = "El stock es obligatorio")      
    @Min(value = 0, message = "El stock no puede ser negativo") 
    private Integer stock;

}
