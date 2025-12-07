package es.juanito.institutos.institutos.models;

import es.juanito.institutos.estudiantes.models.Estudiante;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate; // Necesario para anioFundacion
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString(exclude = {"estudiantes"}) // Excluir la lista de estudiantes para evitar un bucle
@AllArgsConstructor
@NoArgsConstructor // Requerido por JPA
@Entity
@Table(name = "INSTITUTOS")
public class Instituto {

    // ----------------------------------------------------------------------
    // 🔑 Clave Primaria y Metadatos
    // ----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    // ----------------------------------------------------------------------
    // 🏫 Datos de Negocio
    // ----------------------------------------------------------------------

    @Column(nullable = false, unique = true, length = 10)
    private String codigoInstituto; // Identificador único de negocio

    @Column(nullable = false, length = 100)
    private String nombre;

    // --- CAMPOS AÑADIDOS PARA COMPATIBILIDAD CON MAPPER Y TESTS ---

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 50)
    private String ciudad;

    @Column(length = 15)
    private String telefono;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column
    private Integer numeroProfesores;

    @Column(length = 20)
    private String tipo; // Ej: público, privado, concertado

    @Column
    private LocalDate anioFundacion; // Usamos LocalDate

    // ----------------------------------------------------------------------
    // 👥 Relación: Un Instituto a Muchos Estudiantes
    // ----------------------------------------------------------------------

    // En es.juanito.institutos.institutos.models.Instituto.java

    // En Instituto.java (La entidad que contiene la colección)

    @OneToMany(mappedBy = "instituto",
            fetch = FetchType.LAZY, //  CRÍTICO: Cambiar a LAZY
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private Set<Estudiante> estudiantes = new HashSet<>();

    // ----------------------------------------------------------------------
    // 🕒 Metadatos de Auditoría y Borrado Lógico
    // ----------------------------------------------------------------------

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;

    // ----------------------------------------------------------------------
    // Constructor de Conveniencia (Ajustado a los nuevos campos clave)
    // ----------------------------------------------------------------------

    /**
     * Constructor para la creación de la entidad desde un DTO con datos clave.
     * Los demás campos se establecen con valores por defecto o son nulos.
     * NOTA: Este constructor ya no coincide con el Builder de la entidad. Se recomienda usar el AllArgsConstructor y el Builder de Lombok.
     */
    /*
    public Instituto(String codigoInstituto, String nombre) {
        this.codigoInstituto = codigoInstituto;
        this.nombre = nombre;
    }
    */
}