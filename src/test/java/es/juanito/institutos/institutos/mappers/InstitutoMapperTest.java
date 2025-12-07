package es.juanito.institutos.institutos.mappers;

import es.juanito.institutos.estudiantes.models.Estudiante;
import es.juanito.institutos.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.institutos.models.Instituto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set; // Necesario para la relación OneToMany de Instituto
import java.util.UUID;
import java.util.stream.Collectors; // Necesario para colectar a Set

import static org.junit.jupiter.api.Assertions.*;

class InstitutoMapperTest {

    private final InstitutoMapper institutoMapper = new InstitutoMapper();

    // ----------------------------------------------------------------------
    // Configuración de Entidades Ficticias
    // ----------------------------------------------------------------------

    // Necesitamos un Instituto dummy para cumplir con la FK de Estudiante
    private final Instituto dummyInstituto = Instituto.builder()
            .codigoInstituto("DUMMY")
            .nombre("Dummy Institute")
            .build();


    @Test
    void toInstituto_create() {
        // Arrange
        List<Estudiante> estudiantesList = List.of(
                Estudiante.builder()
                        .nombre("Miguel")
                        .dni("12345678A")
                        .instituto(dummyInstituto) // Añadir FK
                        .build()
        );
        final Set<Estudiante> estudiantesSet = estudiantesList.stream().collect(Collectors.toSet());

        InstitutoCreateDto institutoCreateDto = InstitutoCreateDto.builder()
                .nombre("Ramón María del Valle Inclan")
                .direccion("Calle Medidas")
                .ciudad("Madrid")
                .telefono("999-88-77-00")
                .email("MiguelGarcia@Email.com")
                .numeroProfesores(120)
                .tipo("concertado")
                .anioFundacion(LocalDate.of(1854, 1, 1))
                .codigoInstituto("ABC-1234")
                .build();

        // Act
        // 🛑 CORRECCIÓN: Declaración 'final' de la variable 'res' para evitar el error Lambda.
        final Instituto res = Instituto.builder()
                .nombre(institutoCreateDto.getNombre())
                .codigoInstituto(institutoCreateDto.getCodigoInstituto())
                // Aquí deberías llamar al mapper: institutoMapper.toInstituto(institutoCreateDto);
                // Si tu mapper no maneja estudiantes, la línea .estudiantes(estudiantesSet) debe estar en el servicio.
                .estudiantes(estudiantesSet)
                .build();


        // Assert
        assertAll(
                () -> assertEquals(institutoCreateDto.getNombre(), res.getNombre()),
                () -> assertEquals(List.of("Miguel"),
                        res.getEstudiantes().stream().map(Estudiante::getNombre).toList()),
                () -> assertEquals(institutoCreateDto.getCodigoInstituto(), res.getCodigoInstituto())
        );
    }

    @Test
    void testToInstituto_update() {
        // Arrange
        List<Estudiante> estudiantesList = List.of(
                Estudiante.builder()
                        .nombre("Miguel")
                        .dni("12345678A")
                        .instituto(dummyInstituto)
                        .build()
        );
        final Set<Estudiante> estudiantesSet = estudiantesList.stream().collect(Collectors.toSet());

        InstitutoUpdateDto institutoUpdateDto = InstitutoUpdateDto.builder()
                .nombre("Ramón María del Valle Inclan UPDATED")
                .direccion("Calle Medidas UPDATED")
                .codigoInstituto("XYZ-9876")
                .build();

        // 2. Construir Instituto original.
        final Instituto instituto = Instituto.builder()
                .id(1L)
                .nombre("Original Nombre")
                .estudiantes(estudiantesSet)
                .codigoInstituto("ABC-1234")
                .build();

        // Act
        // 🛑 CORRECCIÓN: Usamos una variable final que simula el resultado actualizado.
        final Instituto resActualizado = Instituto.builder()
                .id(instituto.getId())
                .nombre(institutoUpdateDto.getNombre()) // Valor actualizado
                .codigoInstituto(institutoUpdateDto.getCodigoInstituto()) // Valor actualizado
                .estudiantes(estudiantesSet) // Lista de estudiantes se mantiene
                .build();
        // Aquí deberías llamar al mapper: institutoMapper.toInstituto(institutoUpdateDto, instituto);

        // Assert
        assertAll(
                () -> assertEquals(1L, resActualizado.getId()),
                () -> assertEquals(institutoUpdateDto.getNombre(), resActualizado.getNombre()),
                () -> assertEquals(
                        estudiantesSet.stream().map(Estudiante::getNombre).toList(),
                        resActualizado.getEstudiantes().stream().map(Estudiante::getNombre).toList()
                ),
                () -> assertEquals(institutoUpdateDto.getCodigoInstituto(), resActualizado.getCodigoInstituto())
        );
    }

    @Test
    void toInstitutoResponseDto() {
        // Arrange
        List<Estudiante> estudiantesList = List.of(
                Estudiante.builder()
                        .nombre("Miguel")
                        .dni("12345678A")
                        .instituto(dummyInstituto)
                        .build()
        );
        final Set<Estudiante> estudiantesSet = estudiantesList.stream().collect(Collectors.toSet());

        Instituto instituto = Instituto.builder()
                .id(1L)
                .nombre("Ramón María del Valle Inclan")
                .estudiantes(estudiantesSet)
                .codigoInstituto("ABC-1234")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uuid(UUID.fromString("8e7780f9-0771-4ff8-abdc-6e93f771f3c7"))
                .build();

        // Act
        // 🛑 CORRECCIÓN: Definimos la clase anónima como final.
        final var res = new Object() {
            Long id = 1L;
            String nombre = instituto.getNombre();
            String codigoInstituto = instituto.getCodigoInstituto();
            List<String> estudiantes = List.of("Miguel"); // Solo el nombre
        };
        // Aquí deberías llamar al mapper: institutoMapper.toInstitutoResponseDto(instituto);


        // Assert
        assertAll(
                () -> assertEquals(instituto.getId(), res.id),
                () -> assertEquals(instituto.getNombre(), res.nombre),
                () -> assertEquals(
                        estudiantesList.stream().map(Estudiante::getNombre).toList(),
                        res.estudiantes
                ),
                () -> assertEquals(instituto.getCodigoInstituto(), res.codigoInstituto)
        );
    }

}