package es.juanito.institutos.institutos.services;

import es.juanito.institutos.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.institutos.exceptions.InstitutoBadUuidException;
import es.juanito.institutos.institutos.exceptions.InstitutoNotFoundException;
import es.juanito.institutos.institutos.mappers.InstitutoMapper;
import es.juanito.institutos.institutos.models.Instituto;
import es.juanito.institutos.institutos.repositories.InstitutosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Integra Mockito con JUnit5 para poder usar mocks, espías y capturadores en los tests
@ExtendWith(MockitoExtension.class)
class InstitutosServiceImplTest {

    private final Instituto instituto1 = Instituto.builder()
            .id(1L)
            .nombre("Ramón María del Valle Inclan")
            .direccion("Calle Medidas")
            .ciudad("Madrid")
            .telefono("999-88-77-00")
            .email("MiguelGarcia@Email.com")
            .numeroEstudiantes(2458)
            .numeroProfesores(120)
            .tipo("concertado")
            .anioFundacion(LocalDate.of(1854,1,1))
            .createdAt(LocalDateTime.now())
            .updateAt(LocalDateTime.now())
            .uuid(UUID.fromString("51af0a67-ff4b-42f3-8bc3-9db6531d4985"))
            .build();

    private final Instituto instituto2 = Instituto.builder()
            .id(2L)
            .nombre("Jesús y María")
            .direccion("García Noblejas")
            .ciudad("Madrid")
            .telefono("000-11-22-33")
            .email("MariusGutierrez@Email.com")
            .numeroEstudiantes(888)
            .numeroProfesores(46)
            .tipo("privado")
            .anioFundacion(LocalDate.of(2000,12,31))
            .createdAt(LocalDateTime.now())
            .updateAt(LocalDateTime.now())
            .uuid(UUID.fromString("8e7780f9-0771-4ff8-abdc-6e93f771f3c7"))
            .build();
    private InstitutoResponseDto institutoResponse1;

    // usamos el repositorio totalmente simulado
    @Mock
    private InstitutosRepository institutosRepository;
    // usamos el mapper real aunque en modo espía que nos permite simular algunas partes del mismo
    @Spy
    private InstitutoMapper institutoMapper;
    // Es la clase que se testea y a la que se inyectan los mocks y espías automáticamente
    @InjectMocks
    private InstitutosServiceImpl institutosService;
    // Capturador de argumentos
    @Captor
    private ArgumentCaptor<Instituto> institutoCaptor;

    @BeforeEach
    void setUp() {
        institutoResponse1 = institutoMapper.toinstitutoResponseDto(instituto1);
        // Quizá no la necesitemos
        // institutoResponse2 = institutoMapper.toinstitutoResponseDto(instituto2);
    }

    @Test
    void findAll_ShouldReturnAllInstitutos_WhenNoParametersProvided() {
        // Arrange
        List<Instituto> expectedInstituto = Arrays.asList(instituto1, instituto2);
        List<InstitutoResponseDto> expectedInstitutoResponse = institutoMapper.toResponseDtoList(expectedInstituto);
        when(institutosRepository.findAll()).thenReturn(expectedInstituto);

        // Act
        List<InstitutoResponseDto> actualInstitutoResponse = institutosService.findAll(null,null);
        // Assert
        assertIterableEquals(expectedInstitutoResponse,actualInstitutoResponse);
        // Verify
        // verifica que el findAll() se ejecuta una vez
        verify(institutosRepository, times(1)).findAll();
    }
    @Test
    void findAll_ShouldReturnInstitutosByCiudad_WhenParameterProvided()  {
        // Arrange
        String ciudad = "Madrid";
        List<Instituto> expectedInstituto = List.of(instituto1);
        List<InstitutoResponseDto> expectedInstitutoResponse = institutoMapper.toResponseDtoList(expectedInstituto);
        when(institutosRepository.findByCiudad(ciudad)).thenReturn(expectedInstituto);

        //Act
        List<InstitutoResponseDto> actualInstitutoResponse = institutosService.findAll(ciudad, null);

        //Assert
        assertIterableEquals(expectedInstitutoResponse,actualInstitutoResponse);

        // Verify
        // Verifica que solo se ejecuta este método
        verify(institutosRepository, only()).findByCiudad(ciudad);
    }
    @Test
    void findAll_ShouldReturnInstitutosByNombre_WhenParametersProvided() {
        // Arrange
        String nombre = "Ramón María del Valle Inclan";
        List<Instituto> expectedInstituto = List.of(instituto1);
        List<InstitutoResponseDto> expectedInstitutoResponse = institutoMapper.toResponseDtoList(expectedInstituto);
        when(institutosRepository.findByNombreContainsIgnoreCase(nombre)).thenReturn(expectedInstituto);

        // Act
        List<InstitutoResponseDto> actualInstitutoResponse = institutosService.findAll(null, nombre);

        // Assert
        assertIterableEquals(expectedInstitutoResponse,actualInstitutoResponse);

        // Verify
        verify(institutosRepository, only()).findByNombreContainsIgnoreCase(nombre);
    }

    @Test
    void findAll_ShouldReturnInstitutosByCiudadAndNombre_WhenBothParametersProvided() {
        // Arrange
        String ciudad = "Madrid";
        String nombre = "Ramón María del Valle Inclan";
        List<Instituto> expectedInstituto = List.of(instituto1);
        List<InstitutoResponseDto> expectedInstitutoResponse = institutoMapper.toResponseDtoList(expectedInstituto);
        when(institutosRepository.findByCiudadAndNombreContainsIgnoreCase(ciudad,nombre)).thenReturn(expectedInstituto);

        // Act
        List<InstitutoResponseDto> actualInstitutoResponse = institutosService.findAll(ciudad, nombre);

        // Assert
        assertIterableEquals(expectedInstitutoResponse,actualInstitutoResponse);

        // Verify
        verify(institutosRepository, only()).findByCiudadAndNombreContainsIgnoreCase(ciudad, nombre);
    }

    @Test
    void findById_ShouldReturnInstituto_WhenValidIdProvided() {
        // Arrange
        Long id = 1L;
        InstitutoResponseDto expectedInstitutoResponse = institutoResponse1;
        when(institutosRepository.findById(id)).thenReturn(Optional.of(instituto1));

        // Act
        InstitutoResponseDto actualInstitutoResponse = institutosService.findById(id);

        // Assert
        assertEquals(expectedInstitutoResponse, actualInstitutoResponse);

        // Verify
        verify(institutosRepository, only()).findById(id);
    }

    @Test
    void findById_ShouldThrowInstitutoNotFound_WhenInvalidIdProvided() {
        // Arrange
        Long id = 1L;
        when(institutosRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(InstitutoNotFoundException.class, () -> institutosService.findById(id));
        assertEquals("Instituto con id " + id + " no encontrado", res.getMessage());

        // Verify
        // verifica que se ejecuta el método
        verify(institutosRepository).findById(id);
    }


    @Test
    void findByUuid_ShouldReturnInstituto_WhenValidUuidProvided() {
        // Arrange
        UUID expectedUuid = instituto1.getUuid();
        InstitutoResponseDto expectedInstitutoResponse = institutoResponse1;
        when(institutosRepository.findByUuid(expectedUuid)).thenReturn(Optional.of(instituto1));

        // Act
        InstitutoResponseDto actualInstitutoResponse = institutosService.findByUuid(expectedUuid.toString());

        // Assert
        assertEquals(expectedInstitutoResponse, actualInstitutoResponse);

        // Verify
        verify(institutosRepository, only()).findByUuid(expectedUuid);
    }

    @Test
    void findByUuid_ShouldThrowInstitutoNotFound_WhenInvalidUuidProvided() {
        // Arrange
        // String uuid = ""51af0a67-ff4b-42f3-8bc3-9db6531d4985""
        String  uuid = "1234";
        // Act & Assert
        var res = assertThrows(InstitutoBadUuidException.class, () -> institutosService.findByUuid(uuid));

        assertEquals("El UUID " + uuid + " no existe en la base de datos", res.getMessage());


        // Verify
        // verifica que no se a ejecutado
        verify(institutosRepository, never()).findByUuid(any());
    }

    @Test
    void save_ShouldReturnSavedInstituto_WhenValidInstitutoCreatedDtoProvided() {
        // Arrange
        InstitutoCreateDto institutoCreateDto =InstitutoCreateDto.builder()
                .ciudad("Galicia")
                .nombre("Las Meigas")
                .anioFundacion(LocalDate.of(2025,12,31))
                .direccion("Calle Barlovento")
                .tipo("publico")
                .build();
        Instituto expectedInstituto = Instituto.builder()
                .id(1L)
                .ciudad("Galicia")
                .nombre("Las Meigas")
                .anioFundacion(LocalDate.of(2025,12,31))
                .direccion("Calle Barlovento")
                .tipo("publico")
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .build();
        InstitutoResponseDto expectedResponse = institutoMapper.toinstitutoResponseDto(expectedInstituto);

        when(institutosRepository.save(any(Instituto.class))).thenReturn(expectedInstituto);

        // Act
        InstitutoResponseDto actualResponse = institutosService.save(institutoCreateDto);

        // Assert
        assertEquals(expectedResponse, actualResponse);

        // Verify
        verify(institutosRepository).save(institutoCaptor.capture());

        Instituto institutoCaptured = institutoCaptor.getValue();
        assertEquals(expectedInstituto.getCiudad(), institutoCaptured.getCiudad());
        // equivalente con AsssertJ en lugar de JUnit
        //assertThat(institutoCaptured.getCiudad()).isEqualTo(expectedInstituto.getCiudad());
    }

    @Test
    void update_ShouldReturnUpdatedInstituto_WhenValidIdAndInstitutoUpdatedDtoProvided() {
        // Arrange
        Long id = 1L;
        Integer numeroEstudiantes = 2458;
        when(institutosRepository.findById(id)).thenReturn(Optional.of(instituto1));

        InstitutoUpdateDto institutoUpdateDto = InstitutoUpdateDto.builder()
                .numeroEstudiantes(numeroEstudiantes)
                .build();
        Instituto institutoUpdate = institutoMapper.toInstituto(institutoUpdateDto, instituto1);
        when(institutosRepository.save(any(Instituto.class))).thenReturn(instituto1);

        institutoResponse1.setNumeroEstudiantes(numeroEstudiantes);
        InstitutoResponseDto expectedInstitutoResponse = institutoResponse1;

        // Act
        InstitutoResponseDto actualInstitutoResponse = institutosService.update(id, institutoUpdateDto);


        // Assert
        // con Junit da error
        // assertEquals(expectedInstitutoResponse, actualInstitutoResponse);
        // con AssertJ podemos excluir algún campo    }
        assertThat(actualInstitutoResponse)
                .usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(expectedInstitutoResponse);
        verify(institutosRepository).findById(id);
        verify(institutosRepository).save(any());
    }
    @Test
    void update_ShouldThrowInstitutoNotFound_WhenInvalidIdProvided() {
        // Arrange
        Long id = 1L;
        InstitutoUpdateDto institutoUpdateDto = InstitutoUpdateDto.builder()
                .numeroEstudiantes(1450)
                .build();
        when(institutosRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        // con AssertJ
        assertThatThrownBy(
                () -> institutosService.update(id, institutoUpdateDto))
                .isInstanceOf(InstitutoNotFoundException.class)
                .hasMessage("Instituto con id " + id + " no encontrado");
        // con JUnit
        //var res = assertThrows(InstitutoNotFoundException.class,
        //    () -> institutosService.update(id, institutoUpdateDto));
        //assertEquals("Instituto con id " + id + " no encontrado", res.getMessage());

        // Verify
        verify(institutosRepository).findById(id);
        verify(institutosRepository, never()).save(any());
    }
    @Test
    void deleteById_ShouldDeleteInstituto_WhenValidIdProvided() {
        // Arrange
        Long id = 1L;
        when(institutosRepository.findById(id)).thenReturn(Optional.of(instituto1));

        // Act
        // conassertJ
        assertThatCode(() -> institutosService.deleteById(id))
                .doesNotThrowAnyException();

        // Assert
        verify(institutosRepository).deleteById(id);
    }
    @Test
    void deleteById_ShouldThrowInstitutoNotFound_WhenInvalidIdProvided() {
        // Arrange
        Long id = 1L;
        when(institutosRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        // con JUnit
        //var res = assertThrows(InstitutoNotFoundException.class, () -> InstitutosService.deleteById(id));
        //assertEquals("Instituto con id " + id + " no encontrado", res.getMessage());
        // El equivalente con AssertJ
        assertThatThrownBy(() -> institutosService.deleteById(id))
        .isInstanceOf(InstitutoNotFoundException.class)
        .hasMessage("Instituto con id " + id + " no encontrado");

        // Verify
        verify(institutosRepository, never()).deleteById(id);
    }
}