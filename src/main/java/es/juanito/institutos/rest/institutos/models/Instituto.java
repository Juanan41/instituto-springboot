package es.juanito.institutos.rest.institutos.models;


import es.juanito.institutos.rest.estudiantes.models.Estudiante;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INSTITUTOS")
public class Instituto {

    // ===========================
    // 🔑 CLAVES
    // ===========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false, nullable = false, length = 36)
    private UUID uuid;

    @PrePersist
    protected void onCreate() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }

    // ===========================
    // 🏫 DATOS DE NEGOCIO
    // ===========================

    @Column(name = "codigo_instituto", length = 20, unique = true, nullable = false)
    private String codigoInstituto;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 50)
    private String ciudad;

    @Column(length = 15)
    private String telefono;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "NUMERO_PROFESORES", nullable = false)
    private Integer numeroProfesores;

    @Column(length = 20)
    private String tipo;

    @Column(name = "anio_fundacion")
    private LocalDate anioFundacion;

    // ===========================
    // 👥 RELACIONES
    // ===========================

    @OneToMany(
            mappedBy = "instituto",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private Set<Estudiante> estudiantes = new HashSet<>();

    // ===========================
    // 🕒 AUDITORÍA + SOFT DELETE
    // ===========================

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
}
