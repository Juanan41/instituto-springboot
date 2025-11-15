package es.juanito.institutos.institutos.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // JPA necesita un constructor vacío
@Entity
@Table(name = "INSTITUTOS")

public class Instituto {

    @Id // Indicamos que es el ID de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Indicamos que es autoincremental y por el script de datos
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;               // Nombre del instituto

    @Column(nullable = false, length = 100)
    private String ciudad;               // Ciudad donde se encuentra

    @Column(nullable = false, length = 100)
    private String direccion;            // Dirección completa

    @Column(nullable = false, length = 20)
    private String telefono;             // Teléfono de contacto

    @Column(nullable = false, length = 100)
    private String email;                // Email institucional

    @Column(nullable = false)
    private Integer numeroEstudiantes;   // Número de estudiantes

    @Column(nullable = false)
    private Integer numeroProfesores;    // Número de profesores

    @Column(nullable = false, length = 50)
    private String tipo;                 // Tipo: público, privado, concertado

    @Column(nullable = false)
    private LocalDate anioFundacion;     // Año de fundación

    @Column(nullable = false, length = 50)
    private String codigoInstituto;

    @Builder.Default
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateAt = LocalDateTime.now();

    @Column(unique = true, updatable = false, nullable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;
}


