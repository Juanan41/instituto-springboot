package es.juanito.institutos.institutos.models;


import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Instituto {

   // @Id // Indicamos que es el ID de la tabla
   // @GeneratedValue(strategy = GenerationType.IDENTITY)// Indicamos que es autoincremental y por el script de datos

    private Long id;

    //@Column(nullable = false, length = 100)
    private String nombre;               // Nombre del instituto

    //@Column(nullable = false, length = 100)
    private String ciudad;               // Ciudad donde se encuentra

    //@Column(nullable = false, length = 255)
    private String direccion;            // Dirección completa

    //@Column(nullable = false, length = 20)
    private String telefono;             // Teléfono de contacto

    //@Column(nullable = false, length = 100)
    private String email;                // Email institucional

    //@Column(nullable = false)
    private Integer numeroEstudiantes;   // Número de estudiantes

    //@Column(nullable = false)
    private Integer numeroProfesores;    // Número de profesores

    //@Column(nullable = false, length = 50)
    private String tipo;                 // Tipo: público, privado, concertado

    //@Column(nullable = false)
    private LocalDate anioFundacion;     // Año de fundación

    private String codigoInstituto;

    //@Column(updatable = false, nullable = false,
            //columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    //@Column(nullable = false,
            //columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateAt;

    //@Column(unique = true, updatable = false, nullable = false)
    private UUID uuid;

    //@Column(columnDefinition = "boolean default false")
    private Boolean isDeleted;
}


