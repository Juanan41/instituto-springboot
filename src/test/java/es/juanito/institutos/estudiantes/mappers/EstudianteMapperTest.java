package es.juanito.institutos.estudiantes.mappers;

import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.models.Estudiante;
import es.juanito.institutos.institutos.models.Instituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // Necesario para inyectar la clase EstudianteMapper (@Component)
class EstudianteMapperTest {

    // Inyectamos la instancia real de la clase Mapper
    @Autowired
    private EstudianteMapper estudianteMapper;

    // Entidades Mock para Arrange
    private Instituto instituto1;
    private Estudiante estudiante1;
    private Estudiante estudiante2;
    private EstudianteRequestDto createDto;
    private EstudianteRequestDto updateDtoInput; // DTO para la prueba de actualización

    @BeforeEach
    void setUp() {
        // Aseguramos que el Instituto tiene el código para la FK
        instituto1 = Instituto.builder()
                .id(1L)
                .nombre("IES Test")
                .codigoInstituto("I-TEST")
                .uuid(UUID.randomUUID())
                .build();

        estudiante1 = Estudiante.builder()
                .id(1L)
                .nombre("Ana").apellidos("García").dni("11111111A")
                .email("ana@test.com").fechaNacimiento(LocalDate.of(2000, 1, 1))
                .instituto(instituto1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .build();

        estudiante2 = Estudiante.builder()
                .id(2L).nombre("Carlos").apellidos("Pérez").dni("22222222B")
                .email("carlos@test.com").fechaNacimiento(LocalDate.of(2001, 1, 1))
                .instituto(instituto1)
                .build();

        // DTO de CREACIÓN (Entrada)
        createDto = EstudianteRequestDto.builder()
                .nombre("NUEVO").apellidos("ALUMNO").dni("99999999Z")
                .email("nuevo@test.com").fechaNacimiento(LocalDate.of(2005, 5, 5))
                .codigoInstituto("I-TEST") // Usa el código, no el ID
                .build();

        // DTO de ACTUALIZACIÓN (Solo con los campos que cambiarían)
        updateDtoInput = EstudianteRequestDto.builder()
                .nombre("CAMBIADO").apellidos("RAMIREZ")
                .email("cambiado@test.com")
                .dni(estudiante1.getDni()) // Mantenemos el DNI
                .codigoInstituto("I-TEST")
                .build();
    }


    // ----------------------------------------------------------------------
    // 1. Mapeo de Creación: DTO a Entidad (toEstudiante)
    // ----------------------------------------------------------------------

    @Test
    void whenToEstudiante_thenReturnNewEstudiante() {
        // Act
        Estudiante mappedEstudiante = estudianteMapper.toEstudiante(createDto, instituto1);

        // Assert
        assertAll("whenToEstudiante_thenReturnNewEstudiante",
                () -> assertEquals(createDto.getNombre(), mappedEstudiante.getNombre(), "El nombre debe coincidir"),
                () -> assertEquals(createDto.getDni(), mappedEstudiante.getDni(), "El DNI debe coincidir"),
                () -> assertEquals(createDto.getEmail(), mappedEstudiante.getEmail(), "El email debe coincidir"),
                // Verifica que la relación ManyToOne se mapeó correctamente
                () -> assertEquals(instituto1.getId(), mappedEstudiante.getInstituto().getId(), "El ID del Instituto debe coincidir")
        );
    }

    // ----------------------------------------------------------------------
    // 2. Mapeo de Actualización: DTO + Entidad Actual -> Entidad Actualizada (toEstudiante)
    // ----------------------------------------------------------------------

    @Test
    void whenToEstudianteWithExistingEstudiante_thenReturnUpdatedEstudiante() {
        // Arrange: El nuevo instituto es el mismo (no cambia la FK)
        Instituto nuevoInstituto = instituto1;

        // Act
        // Mapeamos el DTO al estudiante existente (estudiante1)
        Estudiante updatedEstudiante = estudianteMapper.toEstudiante(updateDtoInput, estudiante1, nuevoInstituto);

        // Assert
        assertAll("whenToEstudianteWithExistingEstudiante_thenReturnUpdatedEstudiante",
                // Verifica que los campos actualizados se reflejan
                () -> assertEquals(updateDtoInput.getNombre(), updatedEstudiante.getNombre(), "El nombre debe actualizarse"),
                () -> assertEquals(updateDtoInput.getEmail(), updatedEstudiante.getEmail(), "El email debe actualizarse"),
                // Verifica que los campos NO actualizados (ID original) se mantienen
                () -> assertEquals(estudiante1.getId(), updatedEstudiante.getId(), "El ID debe mantenerse"),
                () -> assertEquals(estudiante1.getDni(), updatedEstudiante.getDni(), "El DNI original debe mantenerse")
        );
    }

    // ----------------------------------------------------------------------
    // 3. Mapeo de Salida: Entidad a DTO (toEstudianteRequestDto)
    // ----------------------------------------------------------------------

    @Test
    void whenToEstudianteRequestDto_thenReturnEstudianteRequestDto() {
        // Act
        EstudianteRequestDto responseDto = estudianteMapper.toEstudianteRequestDto(estudiante1);

        // Assert
        assertAll("whenToEstudianteRequestDto_thenReturnEstudianteRequestDto",
                () -> assertEquals(estudiante1.getNombre(), responseDto.getNombre(), "El nombre debe coincidir"),
                () -> assertEquals(estudiante1.getDni(), responseDto.getDni(), "El DNI debe coincidir"),
                // ✅ CRÍTICO: Verificar que mapea el código del instituto, no el ID
                () -> assertEquals(instituto1.getCodigoInstituto(), responseDto.getCodigoInstituto(), "El código del Instituto debe coincidir")
        );
    }

    // ----------------------------------------------------------------------
    // 4. Mapeo de Lista: Lista de Entidades a Lista de DTOs (toRequestDtoList)
    // ----------------------------------------------------------------------

    @Test
    void whenToRequestDtoList_thenReturnEstudianteRequestDtoList() {
        // Arrange
        List<Estudiante> estudiantes = List.of(estudiante1, estudiante2);

        // Act
        List<EstudianteRequestDto> responseList = estudianteMapper.toRequestDtoList(estudiantes);

        // Assert
        assertAll("whenToRequestDtoList_thenReturnEstudianteRequestDtoList",
                () -> assertEquals(estudiantes.size(), responseList.size(), "El tamaño de la lista debe coincidir"),
                () -> assertEquals(estudiante1.getNombre(), responseList.get(0).getNombre(), "El primer nombre debe coincidir"),
                () -> assertEquals(estudiante2.getNombre(), responseList.get(1).getNombre(), "El segundo nombre debe coincidir")
        );
    }
}