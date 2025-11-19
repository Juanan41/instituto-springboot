package es.juanito.institutos.estudiante.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.juanito.institutos.institutos.models.Instituto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="ESTUDIANTES")
public class Estudiante {

    // ----------------------------------------------------------------------
    // ID AUTOINCREMENTAL
    // ----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----------------------------------------------------------------------
    // CÓDIGO DEL INSTITUTO VINCULADO
    // Debe ser único y no puede estar vacío
    // ----------------------------------------------------------------------
    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "El código del instituto es obligatorio")
    private String codigoInstituto;
    @Column(unique = true, nullable = false,  length = 20)
    private String nombre;

    // ----------------------------------------------------------------------
    // BORRADO LÓGICO
    // ----------------------------------------------------------------------
    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;

    // ----------------------------------------------------------------------
    // FECHA DE CREACIÓN AUTOMÁTICA
    // ----------------------------------------------------------------------
    @Column(updatable = false, nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // ----------------------------------------------------------------------
    // FECHA DE ACTUALIZACIÓN AUTOMÁTICA
    // ----------------------------------------------------------------------
    @Column(nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "instituto_id")
    private Instituto instituto;


    //@JoinColumn(name = "instituto_id", nullable = false)
    //private Instituto instituto;

    // @ManyToOne → indica que muchos estudiantes pueden compartir un mismo instituto.
    //
    //@JoinColumn(name = "instituto_id") → define la columna en la tabla Estudiante que apunta al Instituto.
    //
    //nullable = false → asegura que un estudiante siempre pertenece a un instituto

}
