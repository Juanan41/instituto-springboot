package es.juanito.institutos.estudiantes.controllers;

import es.juanito.institutos.rest.estudiantes.dto.EstudianteInfoResponseDto;
import es.juanito.institutos.rest.estudiantes.dto.EstudianteResponseDto;
import es.juanito.institutos.rest.estudiantes.exceptions.EstudianteConflictException;
import es.juanito.institutos.rest.estudiantes.exceptions.EstudianteNotFoundException;
import es.juanito.institutos.rest.estudiantes.services.EstudianteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // 🔑 DESACTIVA JWT / SECURITY EN TESTS
class EstudiantesRestControllerTest {

    private static final String ENDPOINT = "/api/v1/estudiantes";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstudianteService estudianteService;



    private EstudianteInfoResponseDto dto1;
    private EstudianteInfoResponseDto dto2;

    @BeforeEach
    void setUp() {


        dto1 = EstudianteInfoResponseDto.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .username("ana")
                .nombre("Ana")
                .apellidos("García")
                .dni("11111111A")
                .email("ana@test.com")
                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                .isDeleted(false)
                .institutoId(1L)
                .build();

        dto2 = EstudianteInfoResponseDto.builder()
                .id(2L)
                .uuid(UUID.randomUUID())
                .nombre("Carlos")
                .apellidos("Pérez")
                .dni("22222222B")
                .email("carlos@test.com")
                .fechaNacimiento(LocalDate.of(2001, 1, 1))
                .isDeleted(false)
                .institutoId(1L)
                .build();
    }


    // ---------- GET ALL ----------
    @Test
    void getAll() throws Exception {
        EstudianteResponseDto dto = EstudianteResponseDto.builder()
                .id(1L)
                .nombre("Ana")
                .apellidos("García")
                .dni("11111111A")
                .email("ana@test.com")
                .institutoId(1L)
                .build();

        Page<EstudianteResponseDto> page =
                new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        when(estudianteService.findAll(
                any(),   // Optional<String>
                any(),   // Optional<String>
                any(),   // Optional<String>
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].nombre").value("Ana"));

        verify(estudianteService).findAll(
                any(),
                any(),
                any(),
                any(Pageable.class)
        );
    }


    // ---------- GET BY ID ----------
    @Test
    void getById() throws Exception {
        when(estudianteService.findById(1L)).thenReturn(dto1);

        mockMvc.perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"));

        verify(estudianteService).findById(1L);
    }

    @Test
    void getById_notFound() throws Exception {
        when(estudianteService.findById(99L))
                .thenThrow(new EstudianteNotFoundException(99L));

        mockMvc.perform(get(ENDPOINT + "/99"))
                .andExpect(status().isNotFound());
    }

    // ---------- CREATE ----------
    @Test
    void create() throws Exception {
        EstudianteResponseDto dtoSaved = EstudianteResponseDto.builder()
                .id(10L)
                .nombre("Manuela")
                .apellidos("Vázquez")
                .dni("55555555M")
                .email("manuela@new.com")
                .institutoId(1L)
                .build();

        when(estudianteService.save(any())).thenReturn(dtoSaved);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "manuela",
                          "password": "123456",
                          "nombre": "Manuela",
                          "apellidos": "Vázquez",
                          "dni": "55555555M",
                          "email": "manuela@new.com",
                          "fechaNacimiento": "2001-05-10",
                          "codigoInstituto": "INT-0011"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Manuela"));

        verify(estudianteService).save(any());
    }


    @Test
    void create_conflict() throws Exception {
        when(estudianteService.save(any()))
                .thenThrow(new EstudianteConflictException("existe"));

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "testuser",
                          "password": "123456",
                          "nombre": "Test",
                          "apellidos": "Test",
                          "dni": "11111111A",
                          "email": "conflict@test.com",
                          "fechaNacimiento": "2000-01-01",
                          "codigoInstituto": "INT-0011"
                        }
                        """))
                .andExpect(status().isConflict());

        verify(estudianteService).save(any());
    }


    // ---------- DELETE ----------
    @Test
    void delete() throws Exception {
        doNothing().when(estudianteService).deleteById(1L);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete(ENDPOINT + "/1")
                )
                .andExpect(status().isNoContent());


        verify(estudianteService).deleteById(1L);
    }
}
