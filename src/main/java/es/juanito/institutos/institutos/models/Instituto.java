package es.juanito.institutos.institutos.models;

import es.juanito.institutos.estudiante.models.Estudiante;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor   // JPA necesita constructor vacío
@Entity
@Table(name = "INSTITUTOS")
public class Instituto {

    // ----------------------------------------------------------------------
    // ID AUTOINCREMENTAL
    // ----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----------------------------------------------------------------------
    // NOMBRE DEL INSTITUTO: obligatorio, entre 3 y 20 caracteres
    // ----------------------------------------------------------------------
    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    // ----------------------------------------------------------------------
    // CIUDAD
    // ----------------------------------------------------------------------
    @Column(nullable = false, length = 100)
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    // ----------------------------------------------------------------------
    // DIRECCIÓN COMPLETA
    // ----------------------------------------------------------------------
    @Column(nullable = false, length = 100)
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    // ----------------------------------------------------------------------
    // TELÉFONO: debe tener el formato 999-99-99-99
    // ----------------------------------------------------------------------
    @Column(nullable = false, length = 20)
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{2}-\\d{2}",
            message = "El teléfono debe tener el formato 999-99-99-99")
    private String telefono;

    // ----------------------------------------------------------------------
    // EMAIL INSTITUCIONAL
    // ----------------------------------------------------------------------
    @Column(nullable = false, length = 100)
    @Email(message = "Debe ser un email válido")
    @NotEmpty(message = "El email es obligatorio")
    private String email;

    // ----------------------------------------------------------------------
    // TOTAL ESTUDIANTES
    // ----------------------------------------------------------------------
    @Column(nullable = false)
    @NotNull(message = "El número de estudiantes es obligatorio")
    @Min(value = 1, message = "Debe tener al menos 1 estudiante")
    private Integer estudiantes;

    // ----------------------------------------------------------------------
    // TOTAL PROFESORES
    // ----------------------------------------------------------------------
    @Column(nullable = false)
    @NotNull(message = "El número de profesores es obligatorio")
    @Min(value = 1, message = "Debe tener al menos 1 profesor")
    private Integer numeroProfesores;

    // ----------------------------------------------------------------------
    // TIPO: público, privado o concertado
    // ----------------------------------------------------------------------
    @Column(nullable = false, length = 50)
    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    // ----------------------------------------------------------------------
    // AÑO DE FUNDACIÓN
    // ----------------------------------------------------------------------
    @Column(nullable = false)
    @NotNull(message = "El año de fundación es obligatorio")
    private LocalDate anioFundacion;

    // ----------------------------------------------------------------------
    // CÓDIGO ÚNICO DEL INSTITUTO: requerido por tus endpoints
    // ----------------------------------------------------------------------
    @Column(nullable = false, length = 50, unique = true)
    @NotBlank(message = "El código del instituto es obligatorio")
    private String codigoInstituto;

    // ----------------------------------------------------------------------
    // FECHA DE CREACIÓN - automática
    // ----------------------------------------------------------------------
    @Builder.Default
    @Column(updatable = false, nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ----------------------------------------------------------------------
    // FECHA DE ACTUALIZACIÓN - automática
    // ----------------------------------------------------------------------
    @Column(nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateAt = LocalDateTime.now();

    // ----------------------------------------------------------------------
    // UUID ÚNICO NO MODIFICABLE
    // ----------------------------------------------------------------------
    @Column(unique = true, updatable = false, nullable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    // ----------------------------------------------------------------------
    // BORRADO LÓGICO
    // ----------------------------------------------------------------------
    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;

    // Relación con instituto, muchas estudiantes pueden tener un instituto
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "instituto_id") // FK en Estudiante
    @Builder.Default
    private List<Estudiante> estudiante = new ArrayList<>();



    //@OneToMany(mappedBy = "instituto", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Estudiante> estudiante = new ArrayList<>();
    // @OneToMany → indica que un Instituto puede tener muchos estudiantes.
    //
    //mappedBy = "instituto" → le dice a JPA que la relación está controlada por el atributo instituto de la clase Estudiante (no se crea una tabla extra).
    //
    //cascade = CascadeType.ALL → operaciones como guardar o borrar se propagan a los estudiantes automáticamente.
    //
    //orphanRemoval = true → si quitas un estudiante de la lista, se borra de la base de datos.
    //
    //new ArrayList<>() → siempre inicializamos la lista para que nunca sea null.

}
