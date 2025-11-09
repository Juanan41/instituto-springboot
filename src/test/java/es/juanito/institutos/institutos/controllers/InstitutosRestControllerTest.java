package es.juanito.institutos.institutos.controllers;

import es.juanito.institutos.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.institutos.exceptions.InstitutoNotFoundException;
import es.juanito.institutos.institutos.services.InstitutosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class InstitutosRestControllerTest {

    private final String ENDPOINT = "/api/v1/institutos";

    private final InstitutoResponseDto institutoResponse1 = InstitutoResponseDto.builder()
            .id(1L)
            .nombre("Gomez Moreno")
            .ciudad("Madrid")
            .direccion("Calle Albaida")
            .telefono("777-88-99-00")
            .email("pepito@correo.com")
            .numeroEstudiantes(555)
            .numeroProfesores(20)
            .tipo("publico")
            .anioFundacion(LocalDate.of(1983,12,19))
            .codigoInstituto("4567-XXX")
            .build();

    private final InstitutoResponseDto institutoResponse2 = InstitutoResponseDto.builder()
            .id(2L)
            .nombre("IES Francisco de Quevedo")
            .ciudad("Sevilla")
            .direccion("Avenida de los Poblados")
            .telefono("888-99-00-11")
            .email("manolita@correo.com")
            .numeroEstudiantes(1250)
            .numeroProfesores(60)
            .tipo("privado")
            .anioFundacion(LocalDate.of(1956,6,9))
            .codigoInstituto("6789-ZZZ")
            .build();

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private InstitutosService institutosService;

    @Test
    void getAll() {
        var institutos = List.of(institutoResponse1, institutoResponse2);
        when(institutosService.findAll(null, null)).thenReturn(institutos);

        var result = mockMvcTester.get()
                .uri(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson().satisfies(json -> {
                    assertThat(json).extractingPath("$.length()").isEqualTo(institutos.size());
                    assertThat(json).extractingPath("$[0]").convertTo(InstitutoResponseDto.class).isEqualTo(institutoResponse1);
                    assertThat(json).extractingPath("$[1]").convertTo(InstitutoResponseDto.class).isEqualTo(institutoResponse2);
                });

        verify(institutosService, times(1)).findAll(null, null);
    }

    @Test
    void getAllByCiudad() {
        var institutos = List.of(institutoResponse2);
        String query = "?ciudad=" + institutoResponse2.getCiudad();
        when(institutosService.findAll(anyString(), isNull())).thenReturn(institutos);

        var result = mockMvcTester.get()
                .uri(ENDPOINT + query)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson().satisfies(json -> {
                    assertThat(json).extractingPath("$.length()").isEqualTo(institutos.size());
                    assertThat(json).extractingPath("$[0]").convertTo(InstitutoResponseDto.class).isEqualTo(institutoResponse2);
                });

        verify(institutosService, times(1)).findAll(anyString(), isNull());
    }

    @Test
    void getAllByNombre() {
        var institutos = List.of(institutoResponse2);
        String query = "?nombre=" + institutoResponse2.getNombre();
        when(institutosService.findAll(isNull(), anyString())).thenReturn(institutos);

        var result = mockMvcTester.get()
                .uri(ENDPOINT + query)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson().satisfies(json -> {
                    assertThat(json).extractingPath("$.length()").isEqualTo(institutos.size());
                    assertThat(json).extractingPath("$[0]").convertTo(InstitutoResponseDto.class).isEqualTo(institutoResponse2);
                });

        verify(institutosService, only()).findAll(isNull(), anyString());
    }

    @Test
    void getAllByCiudadAndNombre() {
        var institutos = List.of(institutoResponse2);
        String query = "?ciudad=" + institutoResponse2.getCiudad() + "&nombre=" + institutoResponse2.getNombre();
        when(institutosService.findAll(anyString(), anyString())).thenReturn(institutos);

        var result = mockMvcTester.get()
                .uri(ENDPOINT + query)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson().satisfies(json -> {
                    assertThat(json).extractingPath("$.length()").isEqualTo(institutos.size());
                    assertThat(json).extractingPath("$[0]").convertTo(InstitutoResponseDto.class).isEqualTo(institutoResponse2);
                });

        verify(institutosService, only()).findAll(anyString(), anyString());
    }

    @Test
    void getById_shouldReturnJsonWithInstituto_whenValidIdProvided() {
        Long id = institutoResponse1.getId();
        when(institutosService.findById(id)).thenReturn(institutoResponse1);

        var result = mockMvcTester.get()
                .uri(ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .convertTo(InstitutoResponseDto.class)
                .isEqualTo(institutoResponse1);

        verify(institutosService, only()).findById(anyLong());
    }

    @Test
    void getById_shouldThrowInstitutoNotFound_whenInvalidIdProvided() {
        Long id = 3L;
        when(institutosService.findById(anyLong())).thenThrow(new InstitutoNotFoundException(id));

        var result = mockMvcTester.get()
                .uri(ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatus4xxClientError()
                .hasFailed().failure()
                .isInstanceOf(InstitutoNotFoundException.class)
                .hasMessageContaining("no encontrado");

        verify(institutosService, only()).findById(anyLong());
    }

    @Test
    void updatePartial() {
        // Arrange
        Long id = 1L;

        // RequestBody con valores válidos, incluyendo un código de instituto que pase @InstitutoCode
        String requestBody = """
    {
        "nombre": "Las Meigas",
        "ciudad": "Galicia",
        "direccion": "Calle Barlovento",
        "telefono": "999-88-77-00",
        "email": "contacto@lasmeigas.com",
        "numeroEstudiantes": 245,
        "numeroProfesores": 20,
        "tipo": "publico",
        "anioFundacion": "2000-05-12",
        "codigoInstituto": "LMG-1234"
    }
    """;

        // DTO de respuesta esperando
        var institutoUpdated = InstitutoResponseDto.builder()
                .id(1L)
                .nombre("Las Meigas")
                .ciudad("Galicia")
                .direccion("Calle Barlovento")
                .telefono("999-88-77-00")
                .email("contacto@lasmeigas.com")
                .numeroEstudiantes(245)
                .numeroProfesores(20)
                .tipo("publico")
                .anioFundacion(LocalDate.of(2000, 5, 12))
                .codigoInstituto("LMG-1234")
                .build();

        // Mock del servicio
        when(institutosService.update(anyLong(), any(InstitutoUpdateDto.class)))
                .thenReturn(institutoUpdated);

        // Act
        var result = mockMvcTester.patch()
                .uri("/api/v1/institutos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        // Assert
        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .convertTo(InstitutoResponseDto.class)
                .isEqualTo(institutoUpdated);

        // Verify
        verify(institutosService, only()).update(anyLong(), any(InstitutoUpdateDto.class));
    }



    @Test
    void delete() {
        Long id = 1L;
        doNothing().when(institutosService).deleteById(anyLong());

        var result = mockMvcTester.delete()
                .uri(ENDPOINT + "/" + id)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT);

        verify(institutosService, only()).deleteById(anyLong());
    }

    @Test
    void delete_shouldThrowInstitutoNotFound_whenInvalidIdProvided() {
        Long id = 3L;
        doThrow(new InstitutoNotFoundException(id)).when(institutosService).deleteById(anyLong());

        var result = mockMvcTester.delete()
                .uri(ENDPOINT + "/" + id)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .hasFailed().failure()
                .isInstanceOf(InstitutoNotFoundException.class)
                .hasMessageContaining("no encontrado");

        verify(institutosService, only()).deleteById(anyLong());
    }
}
