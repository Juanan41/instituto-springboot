package es.juanito.institutos.institutos.mappers;

import es.juanito.institutos.estudiante.models.Estudiante;
import es.juanito.institutos.institutos.dto.InstitutoCreateDto;
import es.juanito.institutos.institutos.dto.InstitutoUpdateDto;
import es.juanito.institutos.institutos.models.Instituto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InstitutoMapperTest {


    private final InstitutoMapper institutoMapper = new InstitutoMapper();

    @Test
    void toInstituto_create() {
        // Arrange
        List<Estudiante> estudiantes = List.of(
                Estudiante.builder().nombre("Miguel").build()
        );

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
        Instituto res = institutoMapper.toInstituto(institutoCreateDto, estudiantes.get(0));

        // Assert
        assertAll(
                () -> assertEquals(institutoCreateDto.getNombre(), res.getNombre()),
                () -> assertEquals(institutoCreateDto.getDireccion(), res.getDireccion()),
                () -> assertEquals(institutoCreateDto.getCiudad(), res.getCiudad()),
                () -> assertEquals(institutoCreateDto.getTelefono(), res.getTelefono()),
                () -> assertEquals(institutoCreateDto.getEmail(), res.getEmail()),
                () -> assertEquals(List.of("Miguel"),
                        res.getEstudiantes().stream().map(Estudiante::getNombre).toList()),
                () -> assertEquals(institutoCreateDto.getNumeroProfesores(), res.getNumeroProfesores()),
                () -> assertEquals(institutoCreateDto.getTipo(), res.getTipo()),
                () -> assertEquals(institutoCreateDto.getAnioFundacion(), res.getAnioFundacion()),
                () -> assertEquals(institutoCreateDto.getCodigoInstituto(), res.getCodigoInstituto())
        );
    }

    @Test
    void testToInstituto_update() {
        // Arrange
        List<Estudiante> estudiantes = List.of(
                Estudiante.builder().nombre("Miguel").build()
        );

        InstitutoUpdateDto institutoUpdateDto = InstitutoUpdateDto.builder()
                .nombre("Ramón María del Valle Inclan")
                .direccion("Calle Medidas")
                .ciudad("Madrid")
                .telefono("999-88-77-00")
                .email("MiguelGarcia@Email.com")
                .numeroProfesores(120)
                .tipo("concertado")
                .anioFundacion(LocalDate.of(1854, 1, 1))
                .codigoInstituto("XYZ-9876")
                .build();

        Instituto instituto = Instituto.builder()
                .id(1L)
                .nombre(institutoUpdateDto.getNombre())
                .direccion(institutoUpdateDto.getDireccion())
                .ciudad(institutoUpdateDto.getCiudad())
                .telefono(institutoUpdateDto.getTelefono())
                .email(institutoUpdateDto.getEmail())
                .estudiantes(estudiantes)
                .numeroProfesores(institutoUpdateDto.getNumeroProfesores())
                .tipo(institutoUpdateDto.getTipo())
                .anioFundacion(institutoUpdateDto.getAnioFundacion())
                .codigoInstituto(institutoUpdateDto.getCodigoInstituto())
                .build();

        // Act
        Instituto res = institutoMapper.toInstituto(institutoUpdateDto, instituto);

        // Assert
        assertAll(
                () -> assertEquals(1L, res.getId()),
                () -> assertEquals(institutoUpdateDto.getNombre(), res.getNombre()),
                () -> assertEquals(institutoUpdateDto.getDireccion(), res.getDireccion()),
                () -> assertEquals(institutoUpdateDto.getCiudad(), res.getCiudad()),
                () -> assertEquals(institutoUpdateDto.getTelefono(), res.getTelefono()),
                () -> assertEquals(institutoUpdateDto.getEmail(), res.getEmail()),
                () -> assertEquals(
                        estudiantes.stream().map(Estudiante::getNombre).toList(),
                        res.getEstudiantes().stream().map(Estudiante::getNombre).toList()
                ),
                () -> assertEquals(institutoUpdateDto.getNumeroProfesores(), res.getNumeroProfesores()),
                () -> assertEquals(institutoUpdateDto.getTipo(), res.getTipo()),
                () -> assertEquals(institutoUpdateDto.getAnioFundacion(), res.getAnioFundacion()),
                () -> assertEquals(institutoUpdateDto.getCodigoInstituto(), res.getCodigoInstituto())
        );
    }

    @Test
    void toInstitutoResponseDto() {
        // Arrange
        List<Estudiante> estudiantes = List.of(
                Estudiante.builder().nombre("Miguel").build()
        );

        Instituto instituto = Instituto.builder()
                .id(1L)
                .nombre("Ramón María del Valle Inclan")
                .direccion("Calle Medidas")
                .ciudad("Madrid")
                .telefono("999-88-77-00")
                .email("MiguelGarcia@Email.com")
                .estudiantes(estudiantes)
                .numeroProfesores(120)
                .tipo("concertado")
                .anioFundacion(LocalDate.of(1854, 1, 1))
                .codigoInstituto("ABC-1234")
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .uuid(UUID.fromString("8e7780f9-0771-4ff8-abdc-6e93f771f3c7"))
                .build();

        // Act
        var res = institutoMapper.toInstitutoResponseDto(instituto);

        // Assert
        assertAll(
                () -> assertEquals(instituto.getId(), res.getId()),
                () -> assertEquals(instituto.getNombre(), res.getNombre()),
                () -> assertEquals(instituto.getDireccion(), res.getDireccion()),
                () -> assertEquals(instituto.getCiudad(), res.getCiudad()),
                () -> assertEquals(instituto.getTelefono(), res.getTelefono()),
                () -> assertEquals(instituto.getEmail(), res.getEmail()),
                () -> assertEquals(
                        estudiantes.stream().map(Estudiante::getNombre).toList(),
                        res.getEstudiantes()
                ),
                () -> assertEquals(instituto.getNumeroProfesores(), res.getNumeroProfesores()),
                () -> assertEquals(instituto.getTipo(), res.getTipo()),
                () -> assertEquals(instituto.getAnioFundacion(), res.getAnioFundacion()),
                () -> assertEquals(instituto.getCodigoInstituto(), res.getCodigoInstituto())
        );
    }

}
