package es.juanito.institutos.estudiantes.mappers;

import es.juanito.institutos.rest.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.rest.estudiantes.mappers.EstudianteMapper;
import es.juanito.institutos.rest.estudiantes.models.Estudiante;
import es.juanito.institutos.rest.institutos.models.Instituto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EstudianteMapperTest {

    @Autowired
    private EstudianteMapper estudianteMapper;

    private Instituto instituto1;
    private Estudiante estudiante1;
    private EstudianteRequestDto createDto;
    private EstudianteRequestDto updateDtoInput;

    @BeforeEach
    void setUp() {
        instituto1 = Instituto.builder()
                .id(1L)
                .nombre("IES Test")
                .codigoInstituto("I-TEST")
                .uuid(UUID.randomUUID())
                .build();

        estudiante1 = Estudiante.builder()
                .id(1L)
                .nombre("Ana")
                .apellidos("García")
                .dni("11111111A")
                .email("ana@test.com")
                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                .instituto(instituto1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .build();

        createDto = EstudianteRequestDto.builder()
                .nombre("NUEVO")
                .apellidos("ALUMNO")
                .dni("99999999Z")
                .email("nuevo@test.com")
                .fechaNacimiento(LocalDate.of(2005, 5, 5))
                .codigoInstituto("I-TEST")
                .build();

        updateDtoInput = EstudianteRequestDto.builder()
                .nombre("CAMBIADO")
                .apellidos("RAMIREZ")
                .email("cambiado@test.com")
                .dni(estudiante1.getDni())
                .codigoInstituto("I-TEST")
                .build();
    }

    // ----------------------------------------------------------------------
    // 1. CREACIÓN: DTO → ENTITY
    // ----------------------------------------------------------------------
    @Test
    void whenToEstudiante_thenReturnNewEstudiante() {
        Estudiante mapped = estudianteMapper.toEstudiante(createDto, instituto1);

        assertAll(
                () -> assertEquals(createDto.getNombre(), mapped.getNombre()),
                () -> assertEquals(createDto.getApellidos(), mapped.getApellidos()),
                () -> assertEquals(createDto.getDni(), mapped.getDni()),
                () -> assertEquals(createDto.getEmail(), mapped.getEmail()),
                () -> assertEquals(instituto1.getId(), mapped.getInstituto().getId())
        );
    }

    // ----------------------------------------------------------------------
    // 2. ACTUALIZACIÓN: DTO + ENTITY → ENTITY
    // ----------------------------------------------------------------------
    @Test
    void whenToEstudianteWithExisting_thenReturnUpdatedEstudiante() {
        Estudiante updated =
                estudianteMapper.toEstudiante(updateDtoInput, estudiante1, instituto1);

        assertAll(
                () -> assertEquals(updateDtoInput.getNombre(), updated.getNombre()),
                () -> assertEquals(updateDtoInput.getApellidos(), updated.getApellidos()),
                () -> assertEquals(updateDtoInput.getEmail(), updated.getEmail()),
                () -> assertEquals(estudiante1.getId(), updated.getId()),
                () -> assertEquals(estudiante1.getDni(), updated.getDni())
        );
    }
}
