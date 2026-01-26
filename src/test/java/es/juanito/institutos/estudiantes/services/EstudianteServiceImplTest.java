package es.juanito.institutos.estudiantes.services;

import es.juanito.institutos.rest.estudiantes.dto.EstudianteInfoResponseDto;
import es.juanito.institutos.rest.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.rest.estudiantes.dto.EstudianteResponseDto;
import es.juanito.institutos.rest.estudiantes.exceptions.EstudianteNotFoundException;
import es.juanito.institutos.rest.estudiantes.mappers.EstudianteMapper;
import es.juanito.institutos.rest.estudiantes.models.Estudiante;
import es.juanito.institutos.rest.estudiantes.repositories.EstudianteRepository;
import es.juanito.institutos.rest.institutos.models.Instituto;
import es.juanito.institutos.rest.institutos.repositories.InstitutosRepository;

import es.juanito.institutos.rest.estudiantes.services.EstudianteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstudianteServiceImplTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private InstitutosRepository institutosRepository;

    @Mock
    private EstudianteMapper estudianteMapper;

    @InjectMocks
    private EstudianteServiceImpl estudianteService;

    private Instituto instituto1;
    private Estudiante estudiante1;
    private Estudiante estudiante2;
    private EstudianteRequestDto requestDto;
    private EstudianteResponseDto responseDto1;
    private EstudianteResponseDto responseDto2;
    private Pageable pageable;
    private Page<Estudiante> estudiantePage;
    private EstudianteInfoResponseDto infoDto1;


    @BeforeEach
    void setUp() {
        instituto1 = Instituto.builder()
                .id(1L)
                .codigoInstituto("I-TEST")
                .build();

        estudiante1 = Estudiante.builder()
                .id(1L)
                .dni("11111111A")
                .instituto(instituto1)
                .build();

        estudiante2 = Estudiante.builder()
                .id(2L)
                .dni("22222222B")
                .instituto(instituto1)
                .build();

        infoDto1 = EstudianteInfoResponseDto.builder()
                .id(1L)
                .dni("11111111A")
                .nombre("Ana")
                .apellidos("García")
                .email("ana@test.com")
                .institutoId(1L)
                .build();

        requestDto = EstudianteRequestDto.builder()
                .dni("44444444D")
                .codigoInstituto("I-TEST")
                .build();

        responseDto1 = EstudianteResponseDto.builder()
                .dni("11111111A")
                .build();

        responseDto2 = EstudianteResponseDto.builder()
                .dni("22222222B")
                .build();

        pageable = PageRequest.of(0, 10);
        estudiantePage = new PageImpl<>(List.of(estudiante1, estudiante2), pageable, 2);
    }

    // ---------------- FIND ALL ----------------
    @Test
    void testFindAllSinFiltros() {
        when(estudianteRepository.findAll(pageable))
                .thenReturn(estudiantePage);

        when(estudianteMapper.toResponseDto(any(Estudiante.class)))
                .thenReturn(responseDto1, responseDto2);

        Page<EstudianteResponseDto> result =
                estudianteService.findAll(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        pageable
                );

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        verify(estudianteRepository).findAll(pageable);
    }

    // ---------------- FIND BY ID ----------------
    @Test
    void testFindByIdExistente() {
        when(estudianteRepository.findById(1L))
                .thenReturn(Optional.of(estudiante1));

        when(estudianteMapper.toInfoResponseDto(estudiante1))
                .thenReturn(infoDto1);

        EstudianteInfoResponseDto result = estudianteService.findById(1L);

        assertNotNull(result);
        assertEquals("11111111A", result.getDni());
    }



    @Test
    void testFindByIdNoExiste() {
        when(estudianteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EstudianteNotFoundException.class,
                () -> estudianteService.findById(99L)
        );
    }

    // ---------------- SAVE ----------------
    @Test
    void testSaveCorrecto() {

        when(institutosRepository
                .findByCodigoInstitutoAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(instituto1));

        when(estudianteMapper.toEstudiante(requestDto, instituto1))
                .thenReturn(estudiante1);

        when(estudianteRepository.save(estudiante1))
                .thenReturn(estudiante1);

        when(estudianteMapper.toResponseDto(estudiante1))
                .thenReturn(responseDto1);

        EstudianteResponseDto result =
                estudianteService.save(requestDto);

        assertNotNull(result);
        verify(estudianteRepository).save(estudiante1);
    }


    // ---------------- UPDATE ----------------
    @Test
    void testUpdateCorrecto() {

        when(estudianteRepository.findById(1L))
                .thenReturn(Optional.of(estudiante1));

        when(institutosRepository
                .findByCodigoInstitutoAndIsDeletedFalse(anyString()))
                .thenReturn(Optional.of(instituto1));

        when(estudianteMapper.toEstudiante(requestDto, estudiante1, instituto1))
                .thenReturn(estudiante1);

        when(estudianteRepository.save(estudiante1))
                .thenReturn(estudiante1);

        when(estudianteMapper.toResponseDto(estudiante1))
                .thenReturn(responseDto1);

        EstudianteResponseDto result =
                estudianteService.update(1L, requestDto);

        assertNotNull(result);
    }


    // ---------------- DELETE ----------------
    @Test
    void testDeleteById() {
        when(estudianteRepository.existsById(1L)).thenReturn(true);

        estudianteService.deleteById(1L);

        verify(estudianteRepository).updateIsDeletedToTrueById(1L);
    }
}
