package es.juanito.institutos.estudiantes.controllers;

import es.juanito.institutos.estudiantes.dto.EstudianteRequestDto;
import es.juanito.institutos.estudiantes.exceptions.EstudianteConflictException;
import es.juanito.institutos.estudiantes.exceptions.EstudianteNotFoundException;
import es.juanito.institutos.estudiantes.models.Estudiante; // Importamos la Entidad
import es.juanito.institutos.estudiantes.services.EstudianteService;
import es.juanito.institutos.institutos.models.Instituto; // Necesario para mocks
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import es.juanito.institutos.pagination.utils.PageResponse; // Importa tu DTO de respuesta paginada
import static org.mockito.ArgumentMatchers.any;


import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class EstudiantesRestControllerTest {
    private final String ENDPOINT = "/api/v1/estudiantes";

    // Mocks de Entidades y DTOs
    private Instituto instituto1;
    private Estudiante estudiante1;
    private EstudianteRequestDto dto1;
    private EstudianteRequestDto dto2;

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private EstudianteService estudianteService; // Nombre del servicio corregido

    @BeforeEach
    void setUp() {
        instituto1 = Instituto.builder().id(1L).nombre("IES Test").codigoInstituto("I-TEST").build();

        // Entidad Mock (usada internamente si se necesita)
        estudiante1 = Estudiante.builder()
                .id(1L).nombre("Ana").apellidos("García").dni("11111111A")
                .email("ana@test.com").fechaNacimiento(LocalDate.of(2000, 1, 1))
                .instituto(instituto1)
                .build();

        // DTO de Respuesta/Petición (El contrato del Servicio)
        dto1 = EstudianteRequestDto.builder()
                .id(1L).nombre("Ana").apellidos("García").dni("11111111A")
                .email("ana@test.com").fechaNacimiento(LocalDate.of(2000, 1, 1))
                .codigoInstituto("INT-0011")
                .isDeleted(false)
                .build();

        dto2 = EstudianteRequestDto.builder()
                .id(2L).nombre("Carlos").apellidos("Pérez").dni("22222222B")
                .email("carlos@test.com").fechaNacimiento(LocalDate.of(2001, 1, 1))
                .codigoInstituto("INT-0022")
                .isDeleted(false)
                .build();
    }
    @Test
    void getAll() {
        // Arrange
        var dtoList = List.of(dto1, dto2);
        // Crear la página simulada que devolverá el Servicio (Page<DTO>)
        Pageable pageable = PageRequest.of(0, 10);
        Page<EstudianteRequestDto> mockPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        // Crear el PageResponse que devolverá el Controller (PageResponse<DTO>)
        PageResponse<EstudianteRequestDto> pageResponse = PageResponse.of(mockPage, "nombre", "asc");
        // Mockear la llamada al Servicio con los 3 argumentos esperados (filtros null + Pageable)
        // El servicio devuelve Page<DTO>
        when(estudianteService.findAll(isNull(), isNull(), any(Pageable.class))).thenReturn(mockPage);

        // Act
        var result = mockMvcTester.get()
                .uri(ENDPOINT + "?page=0&size=10&sortBy=nombre&direction=asc")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatusOk()
                .bodyJson().satisfies(json -> {
                    // Assert sobre el contenido de la PageResponse
                    assertThat(json).extractingPath("$.content.length()").isEqualTo(dtoList.size());
                    assertThat(json).extractingPath("$.content[0]")
                            .convertTo(EstudianteRequestDto.class).usingRecursiveComparison().isEqualTo(dto1);
                    assertThat(json).extractingPath("$.totalPages").isEqualTo(1);
                });

        // Verify
        // Verificar la llamada al servicio con los 3 argumentos
        verify(estudianteService, times(1)).findAll(isNull(), isNull(), any(Pageable.class));
    }

    @Test
    void getAllByNombre() {
        // Arrange
        var estudiantes = List.of(dto2);
        String nombre = dto2.getNombre();
        // Crear la página simulada
        Pageable pageable = PageRequest.of(0, 10);
        Page<EstudianteRequestDto> mockPage = new PageImpl<>(estudiantes, pageable, estudiantes.size());
        PageResponse<EstudianteRequestDto> pageResponse = PageResponse.of(mockPage, "nombre", "asc");

        // Mockear la llamada al Servicio con 3 argumentos (filtro nombre + Pageable)
        // Usamos any(String.class) para el código de instituto, ya que es opcional
        when(estudianteService.findAll(isNull(), eq(nombre), any(Pageable.class))).thenReturn(mockPage);

        // Act
        // Añadir parámetros de paginación a la URI
        var result = mockMvcTester.get()
                .uri(ENDPOINT + "?nombre=" + nombre + "&page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatusOk()
                .bodyJson().satisfies(json -> {
                    // Assert sobre el contenido de la PageResponse
                    assertThat(json).extractingPath("$.content.length()").isEqualTo(estudiantes.size());
                    assertThat(json).extractingPath("$.content[0]")
                            .convertTo(EstudianteRequestDto.class).usingRecursiveComparison().isEqualTo(dto2);
                    assertThat(json).extractingPath("$.totalElements").isEqualTo(1);
                });

        // Verify
        // Verificar la llamada al servicio con los 3 argumentos
        verify(estudianteService, times(1)).findAll(isNull(), eq(nombre), any(Pageable.class));
    }

    // --- 2. GET BY ID (findById) ---

    @Test
    void getById() {
        // Arrange
        Long id = dto1.getId();
        when(estudianteService.findById(id)).thenReturn(dto1);

        // Act
        var result = mockMvcTester.get()
                .uri(ENDPOINT + "/" + id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .convertTo(EstudianteRequestDto.class).usingRecursiveComparison().isEqualTo(dto1);

        // Verify
        verify(estudianteService, only()).findById(anyLong());
    }

    @Test
    void getById_shouldThrowEstudianteNotFound_whenInvalidIdProvided() {
        // Arrange
        Long id = 99L;
        when(estudianteService.findById(anyLong())).thenThrow(new EstudianteNotFoundException(id));

        // Act
        var result = mockMvcTester.get()
                .uri(ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatus4xxClientError()
                .hasFailed().failure()
                .isInstanceOf(EstudianteNotFoundException.class)
                .hasMessageContaining("no encontrado");

        // Verify
        verify(estudianteService, only()).findById(anyLong());
    }

    // --- 3. CREATE (POST) ---

    @Test
    void create() {
        // Arrange
        String requestBody = """
                {
                   "nombre": "Manuela",
                   "apellidos": "Vázquez",
                   "dni": "55555555M",
                   "email": "manuela@new.com",
                   "fechaNacimiento": "2002-11-20",
                   "codigoInstituto": "INT-0055" 
                }
                """;

        var dtoSaved = EstudianteRequestDto.builder()
                .id(10L).nombre("Manuela").apellidos("Vázquez")
                .dni("55555555M").email("manuela@new.com")
                .codigoInstituto("INT-0066")
                .isDeleted(false)
                .build();

        when(estudianteService.save(any(EstudianteRequestDto.class))).thenReturn(dtoSaved);

        // Act
        var result = mockMvcTester.post()
                .uri(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(EstudianteRequestDto.class)
                .usingRecursiveComparison()
                .ignoringFields("instituto", "uuid", "createdAt", "updatedAt")
                .isEqualTo(dtoSaved);

        verify(estudianteService, only()).save(any(EstudianteRequestDto.class));
    }

    // ... (Aplica la misma lógica para UPDATE y DELETE) ...
    // ... (Los tests de DELETE no devuelven cuerpo, solo verifican el estado y la llamada) ...

    @Test
    void create_whenBadRequest() {
        // Arrange (Nombre vacío - falla validación @NotBlank)
        String requestBody = """
                {
                   "nombre": null,
                   "dni": "12345678A"
                
                }
                """;

        // Act
        var result = mockMvcTester.post()
                .uri(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        // Assert: Se debe devolver BAD_REQUEST por validación
        assertThat(result)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .hasPathSatisfying("$.errores", path ->
                        assertThat(path).hasFieldOrProperty("nombre"));


        verify(estudianteService, never()).save(any(EstudianteRequestDto.class));
    }

    @Test
    void create_whenDniOrEmailExists() {
        // Arrange
        String requestBody = """
                {
                   "dni": "11111111A",
                   "nombre": "Test",
                   "apellidos": "Ficticios",
                   "email": "nuevo@conflicto.com",
                   "fechaNacimiento": "2000-01-01",
                   "codigoInstituto": "INT-9999"
                
                }
                """;

        when(estudianteService.save(any(EstudianteRequestDto.class)))
                .thenThrow(new EstudianteConflictException("Ya existe un estudiante con el DNI 11111111A o Email"));


        // Act
        var result = mockMvcTester.post()
                .uri(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatus(HttpStatus.CONFLICT)
                .hasFailed().failure()
                .isInstanceOf(EstudianteConflictException.class)
                .hasMessageContaining("existe");


        verify(estudianteService, only()).save(any(EstudianteRequestDto.class));
    }

    // --- 4. UPDATE (PUT) ---

    // En EstudiantesRestControllerTest.java

    @Test
    void update() {
        // Arrange
        Long id = 1L;
        String requestBody = """
            {
               "nombre": "ANA",
               "apellidos": "RAMIREZ",
               "dni": "11111111A",
               "email": "ana@test.com",
               "codigoInstituto": "INT-0044",
               "fechaNacimiento": "2000-01-01"
            }
            """;

        // CREAR EL DTO QUE EL SERVICIO DEBE DEVOLVER
        // Reutilizamos el 'dtoSaved' (o creamos uno nuevo) que usaríamos para la aserción.
        var dtoSaved = EstudianteRequestDto.builder()
                .id(id)
                .nombre("ANA").apellidos("RAMIREZ")
                .dni("11111111A").email("ana@test.com")
                .codigoInstituto("INT-0066")
                .build();


        when(estudianteService.update(eq(id), any(EstudianteRequestDto.class))).thenReturn(dtoSaved);

        // Act
        var result = mockMvcTester.put()
                .uri(ENDPOINT+ "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                // Convertimos al DTO
                .convertTo(EstudianteRequestDto.class)
                .usingRecursiveComparison()
                // Ignoramos campos variables que Lombok no puede inicializar bien en DTOs de prueba
                .ignoringFields("fechaNacimiento", "createdAt", "updatedAt", "uuid", "isDeleted")
                .isEqualTo(dtoSaved);

        verify(estudianteService, only()).update(anyLong(), any(EstudianteRequestDto.class));
    }
    @Test
    void update_shouldThrowEstudianteNotFound() {
        // Arrange
        Long id = 99L;
        String requestBody = """
                {
                   "nombre": "TEST",
                   "dni": "99999999Z",
                   "apellidos": "RAMIREZ",
                   "fechaNacimiento": "2000-11-01",
                   "codigoInstituto": "INT-0055",
                   "email": "eeeee@gmail.com"
                }
                """;
        when(estudianteService.update(anyLong(), any(EstudianteRequestDto.class))).thenThrow(new EstudianteNotFoundException(id));

        // Act
        var result = mockMvcTester.put()
                .uri(ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .hasFailed().failure()
                .isInstanceOf(EstudianteNotFoundException.class)
                .hasMessageContaining("no encontrado");

        // Verify
        verify(estudianteService, only()).update(anyLong(), any());
    }

    // --- 5. DELETE ---

    @Test
    void delete() {
        // Arrange
        Long id = 1L;
        doNothing().when(estudianteService).deleteById(anyLong());

        // Act
        var result = mockMvcTester.delete()
                .uri(ENDPOINT+ "/" + id)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT);

        verify(estudianteService, only()).deleteById(anyLong());
    }


    // El resto de tests de error (NotFound, Conflict, BadRequest) no requieren cambios de tipo de datos.

    @Test
    void delete_shouldThrowEstudianteNotFound() {
        // Arrange
        Long id = 99L;
        doThrow(new EstudianteNotFoundException(id)).when(estudianteService).deleteById(anyLong());

        // Act
        var result = mockMvcTester.delete()
                .uri(ENDPOINT+ "/" + id)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND);

        verify(estudianteService, only()).deleteById(anyLong());
    }
}