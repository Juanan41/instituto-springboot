package es.juanito.institutos.institutos.models;

import es.juanito.institutos.estudiantes.models.Estudiante;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.ColumnDefault;

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

    // ----------------------------------------------------------------------
    //  Clave Primaria y UUID
    // ----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Columna UUID: ya estaba bien
    @Column( unique = true, updatable = false, nullable = false, length = 36)
    private UUID uuid;
    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID(); // Asigna el UUID si es nulo
        }
    }

    // ----------------------------------------------------------------------
    // 🏫 Datos de Negocio
    // ----------------------------------------------------------------------

    // Columna codigo_instituto: ya estaba bien
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

    // **FALLO 5 CORREGIDO**
    @Column(name = "NUMERO_PROFESORES")
    private Integer numeroProfesores;

    @Column(length = 20)
    private String tipo;


    @Column(name = "anio_fundacion") // Usamos Snake Case en minúsculas
    private LocalDate anioFundacion;

    // ----------------------------------------------------------------------
    // 👥 Relación
    // ----------------------------------------------------------------------

    @OneToMany(mappedBy = "instituto",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private Set<Estudiante> estudiantes = new HashSet<>();

    // ----------------------------------------------------------------------
    // 🕒 Metadatos de Auditoría y Borrado Lógico (FALLOS 2, 3, 4 CORREGIDOS)
    // ----------------------------------------------------------------------

    @CreationTimestamp
    @Column( updatable = false, nullable = false) // **FALLO 2 CORREGIDO**
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column( nullable = false) // **FALLO 3 CORREGIDO**
    private LocalDateTime updatedAt;

    @ColumnDefault("FALSE")
    @Column( nullable = false) // **FALLO 4 CORREGIDO**
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