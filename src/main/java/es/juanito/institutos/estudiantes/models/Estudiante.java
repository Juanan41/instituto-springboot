package es.juanito.institutos.estudiantes.models;

import es.juanito.institutos.institutos.models.Instituto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate; // Importar LocalDate para la fecha de nacimiento
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ESTUDIANTES")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----------------------------------------------------------------------
    // 👤 CAMPOS DE DATOS PERSONALES (Añadidos para resolver los errores de 'get')
    // ----------------------------------------------------------------------

    @Column(nullable = false, length = 100)
    private String nombre; // Campo existente

    @Column(nullable = false, length = 100)
    private String apellidos; // <-- Añadido

    @Column(nullable = false, length = 100, unique = true)
    private String email; // <-- Añadido

    @Column(nullable = false)
    private LocalDate fechaNacimiento; // <-- Añadido

    @Column(nullable = false, length = 9, unique = true)
    private String dni; // <-- Añadido

    // ----------------------------------------------------------------------
    // 🏫 Relación ManyToOne
    // ----------------------------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instituto_id", nullable = false)
    private Instituto instituto;

    // ----------------------------------------------------------------------
    // 🕒 Metadatos
    // ----------------------------------------------------------------------
    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "created_at", updatable = false, nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(unique = true, updatable = false, nullable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();
}