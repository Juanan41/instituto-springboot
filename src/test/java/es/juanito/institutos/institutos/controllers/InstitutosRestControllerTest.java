package es.juanito.institutos.institutos.controllers;

import es.juanito.institutos.rest.institutos.controllers.InstitutosRestController;
import es.juanito.institutos.rest.auth.services.jwt.JwtService;
import es.juanito.institutos.config.auth.JwtAuthFilter;
import es.juanito.institutos.rest.institutos.dto.InstitutoResponseDto;
import es.juanito.institutos.rest.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.rest.institutos.exceptions.InstitutoNotFoundException;
import es.juanito.institutos.rest.institutos.services.InstitutosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InstitutosRestController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 DESACTIVA FILTROS DE SEGURIDAD
class InstitutosRestControllerTest {

    private static final String ENDPOINT = "/api/v1/institutos";

    @Autowired
    private MockMvc mockMvc;

    // -------- MOCKS NECESARIOS --------
    @MockBean
    private InstitutosService institutosService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;   // 🔥 CLAVE
    @MockBean
    private JwtService jwtService;         // 🔥 CLAVE

    private final InstitutoResponseDto institutoResponse1 = InstitutoResponseDto.builder()
            .id(1L)
            .nombre("Gomez Moreno")
            .ciudad("Madrid")
            .direccion("Calle Albaida")
            .telefono("777-88-99-00")
            .email("pepito@correo.com")
            .numeroProfesores(20)
            .tipo("publico")
            .anioFundacion(LocalDate.of(1983, 12, 19))
            .codigoInstituto("4567-XXX")
            .build();

    private final InstitutoResponseDto institutoResponse2 = InstitutoResponseDto.builder()
            .id(2L)
            .nombre("IES Francisco de Quevedo")
            .ciudad("Sevilla")
            .direccion("Avenida de los Poblados")
            .telefono("888-99-00-11")
            .email("manolita@correo.com")
            .numeroProfesores(60)
            .tipo("privado")
            .anioFundacion(LocalDate.of(1956, 6, 9))
            .codigoInstituto("6789-ZZZ")
            .build();

    // ---------- GET ALL ----------
    @Test
    void getAll() throws Exception {
        when(institutosService.findAll(null, null))
                .thenReturn(List.of(institutoResponse1, institutoResponse2));

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Gomez Moreno"))
                .andExpect(jsonPath("$[1].nombre").value("IES Francisco de Quevedo"));
    }

    @Test
    void getById_ok() throws Exception {
        when(institutosService.findById(1L))
                .thenReturn(institutoResponse1);

        mockMvc.perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gomez Moreno"));
    }

    @Test
    void getById_notFound() throws Exception {
        when(institutosService.findById(anyLong()))
                .thenThrow(new InstitutoNotFoundException(99L));

        mockMvc.perform(get(ENDPOINT + "/99"))
                .andExpect(status().isNotFound());
    }

    // ---------- PATCH ----------
    @Test
    void updatePartial() throws Exception {
        when(institutosService.update(anyLong(), any(InstitutoUpdateDto.class)))
                .thenReturn(institutoResponse1);

        mockMvc.perform(patch(ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "nombre": "Gomez Moreno",
                              "direccion": "Calle Albaida"
                            }
                            """))
                .andExpect(status().isOk());
    }


    // ---------- DELETE ----------
    @Test
    void deleteById() throws Exception {
        doNothing().when(institutosService).deleteById(anyLong());

        mockMvc.perform(delete(ENDPOINT + "/1"))
                .andExpect(status().isNoContent());
    }
}
