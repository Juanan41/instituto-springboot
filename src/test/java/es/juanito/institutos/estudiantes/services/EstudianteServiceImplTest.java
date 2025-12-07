package es.juanito.institutos.estudiantes.services;

import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.exceptions.EstudianteConflictException;
import es.juanito.institutos.estudiantes.exceptions.EstudianteNotFoundException;
import es.juanito.institutos.estudiantes.mappers.EstudianteMapper;
import es.juanito.institutos.estudiantes.models.Estudiante;
import es.juanito.institutos.estudiantes.repositories.EstudianteRepository;
import es.juanito.institutos.institutos.models.Instituto;
import es.juanito.institutos.institutos.repositories.InstitutosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstudianteServiceImplTest {

    // 🏫 Mocks de Repositorios y Mapper
    @Mock
    private EstudianteRepository estudianteRepository;
    @Mock
    private InstitutosRepository institutosRepository;
    @Mock
    private EstudianteMapper estudianteMapper;

    @InjectMocks
    private EstudianteServiceImpl estudianteService;

    // 🌐 Datos Mock para Setup
    private Instituto instituto1;
    private Estudiante estudiante1;
    private Estudiante estudiante2; // Declaramos estudiante2 a nivel de clase
    private EstudianteRequestDto requestDto1;
    private EstudianteRequestDto requestDto2;
    private List<Estudiante> estudiantesList;
    private List<EstudianteRequestDto> dtoList;

    @BeforeEach
    void setUp() {
        instituto1 = Instituto.builder().id(1L).nombre("IES Test").codigoInstituto("I-TEST").build();

        estudiante1 = Estudiante.builder()
                .id(1L).nombre("Ana").apellidos("García").dni("11111111A")
                .email("ana@test.com").fechaNacimiento(LocalDate.of(2000, 1, 1))
                .instituto(instituto1)
                .build();

        estudiante2 = Estudiante.builder() // Definimos la variable de clase
                .id(2L).nombre("Carlos").apellidos("Pérez").dni("22222222B")
                .email("carlos@test.com").fechaNacimiento(LocalDate.of(2001, 1, 1))
                .instituto(instituto1)
                .build();

        requestDto1 = EstudianteRequestDto.builder()
                .nombre("Ana").apellidos("García").dni("11111111A")
                .email("ana@test.com").fechaNacimiento(LocalDate.of(2000, 1, 1))
                .codigoInstituto("I-TEST").build();

        requestDto2 = EstudianteRequestDto.builder()
                .nombre("Nuevo").apellidos("Alumno").dni("44444444D")
                .email("nuevo@test.com").fechaNacimiento(LocalDate.of(2003, 3, 3))
                .codigoInstituto("I-TEST").build();

        estudiantesList = List.of(estudiante1, estudiante2);
        dtoList = List.of(requestDto1, requestDto2);
    }

    // ----------------------------------------------------------------------
    // 1. FIND ALL (Devuelve DTO)
    // ----------------------------------------------------------------------

    @Test
    void testFindAll_NoFiltros() {
        // Arrange
        when(estudianteRepository.findAll()).thenReturn(estudiantesList);
        // ✅ Mockear el mapeo de Entidad a DTO (según el contrato del servicio)
        when(estudianteMapper.toRequestDtoList(estudiantesList)).thenReturn(dtoList);

        // Act
        // ✅ CORRECCIÓN: Esperamos List<EstudianteRequestDto>
        List<EstudianteRequestDto> result = estudianteService.findAll(null, null);

        // Assert
        assertAll("findAll",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size())
        );

        // Verify
        verify(estudianteRepository, times(1)).findAll();
        verify(estudianteMapper, times(1)).toRequestDtoList(estudiantesList);
    }

    @Test
    void testFindAll_PorCodigoInstituto() {
        // Arrange
        String codigo = "I-TEST";
        when(estudianteRepository.findByInstitutoCodigoInstitutoContainsIgnoreCase(codigo)).thenReturn(estudiantesList);
        // ✅ Mockear el mapeo
        when(estudianteMapper.toRequestDtoList(estudiantesList)).thenReturn(dtoList);

        // Act
        // ✅ CORRECCIÓN: Esperamos List<EstudianteRequestDto>
        List<EstudianteRequestDto> result = estudianteService.findAll(codigo, null);

        // Assert
        assertAll("findAll_PorCodigo",
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size())
        );

        // Verify
        verify(estudianteRepository, times(1)).findByInstitutoCodigoInstitutoContainsIgnoreCase(codigo);
        verify(estudianteMapper, times(1)).toRequestDtoList(estudiantesList);
    }

    // ----------------------------------------------------------------------
    // 2. FIND BY ID (Devuelve DTO)
    // ----------------------------------------------------------------------

    @Test
    void testFindById_Existente() {
        // Arrange
        Long id = 1L;
        when(estudianteRepository.findById(id)).thenReturn(Optional.of(estudiante1));
        // ✅ Mockear el mapeo a DTO
        when(estudianteMapper.toEstudianteRequestDto(estudiante1)).thenReturn(requestDto1);

        // Act
        // ✅ CORRECCIÓN: Esperamos EstudianteRequestDto
        EstudianteRequestDto result = estudianteService.findById(id);

        // Assert
        assertAll("findById_Existente",
                () -> assertNotNull(result),
                () -> assertEquals(requestDto1.getDni(), result.getDni())
        );

        // Verify
        verify(estudianteRepository, times(1)).findById(id);
        verify(estudianteMapper, times(1)).toEstudianteRequestDto(estudiante1);
    }

    @Test
    void testFindById_NoExiste() {
        // Arrange
        Long id = 99L;
        when(estudianteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EstudianteNotFoundException.class, () -> estudianteService.findById(id));

        // Verify
        verify(estudianteRepository, times(1)).findById(id);
    }

    // ----------------------------------------------------------------------
    // 3. SAVE (CREATE) (Devuelve DTO)
    // ----------------------------------------------------------------------

    @Test
    void testSave_Exitoso() {
        // Arrange
        when(institutosRepository.findByCodigoInstituto(anyString())).thenReturn(Optional.of(instituto1));
        when(estudianteMapper.toEstudiante(eq(requestDto2), eq(instituto1))).thenReturn(estudiante1);
        when(estudianteRepository.save(estudiante1)).thenReturn(estudiante1);
        when(estudianteMapper.toEstudianteRequestDto(estudiante1)).thenReturn(requestDto1);

        // Act
        EstudianteRequestDto result = estudianteService.save(requestDto2);

        // Assert
        assertAll("save_Exitoso",
                () -> assertNotNull(result),
                () -> assertEquals(requestDto1.getDni(), result.getDni())
        );

        // Verify
        verify(institutosRepository, times(1)).findByCodigoInstituto(requestDto2.getCodigoInstituto());
        verify(estudianteRepository, times(1)).save(estudiante1);
    }

    @Test
    void testSave_InstitutoNoEncontrado() {
        // Arrange
        when(institutosRepository.findByCodigoInstituto(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EstudianteConflictException.class, () -> estudianteService.save(requestDto2));

        // Verify
        verify(institutosRepository, times(1)).findByCodigoInstituto(requestDto2.getCodigoInstituto());
        verify(estudianteRepository, never()).save(any());
    }

    // ----------------------------------------------------------------------
    // 4. UPDATE (Devuelve DTO)
    // ----------------------------------------------------------------------

    @Test
    void testUpdate_Exitoso() {
        // Arrange
        Long id = 1L;
        when(estudianteRepository.findById(id)).thenReturn(Optional.of(estudiante1));
        when(institutosRepository.findByCodigoInstituto(anyString())).thenReturn(Optional.of(instituto1));
        when(estudianteMapper.toEstudiante(eq(requestDto2), eq(estudiante1), eq(instituto1))).thenReturn(estudiante1);
        when(estudianteRepository.save(estudiante1)).thenReturn(estudiante1);
        when(estudianteMapper.toEstudianteRequestDto(estudiante1)).thenReturn(requestDto2);

        // Act
        EstudianteRequestDto result = estudianteService.update(id, requestDto2);

        // Assert
        assertAll("update_Exitoso",
                () -> assertNotNull(result),
                () -> assertEquals(requestDto2.getNombre(), result.getNombre())
        );

        // Verify
        verify(estudianteRepository, times(1)).findById(id);
        verify(estudianteRepository, times(1)).save(estudiante1);
    }

    // ----------------------------------------------------------------------
    // 5. DELETE BY ID (Soft Delete)
    // ----------------------------------------------------------------------

    @Test
    void testDeleteById_Exitoso() {
        // Arrange
        Long id = 1L;
        when(estudianteRepository.existsById(id)).thenReturn(true);
        doNothing().when(estudianteRepository).updateIsDeletedToTrueById(id);

        // Act
        estudianteService.deleteById(id);

        // Verify
        verify(estudianteRepository, times(1)).existsById(id);
        verify(estudianteRepository, times(1)).updateIsDeletedToTrueById(id);
    }
}