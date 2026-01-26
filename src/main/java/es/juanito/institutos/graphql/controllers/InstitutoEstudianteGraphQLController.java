package es.juanito.institutos.graphql.controllers;

import es.juanito.institutos.rest.estudiantes.models.Estudiante;
import es.juanito.institutos.rest.estudiantes.repositories.EstudianteRepository;
import es.juanito.institutos.rest.institutos.models.Instituto;
import es.juanito.institutos.rest.institutos.repositories.InstitutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class InstitutoEstudianteGraphQLController {

    private final EstudianteRepository estudianteRepository;
    private final InstitutosRepository institutosRepository;

    // -----------------------
    // --- QUERIES ---
    // -----------------------

    @QueryMapping
    public List<Estudiante> estudiantes() {
        return estudianteRepository.findAll();
    }

    @QueryMapping
    public Estudiante estudianteById(@Argument Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Instituto> institutos() {
        return institutosRepository.findAll();
    }

    @QueryMapping
    public Instituto institutoById(@Argument Long id) {
        return institutosRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Estudiante> estudiantesByNombre(@Argument String nombre) {
        return estudianteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @QueryMapping
    public List<Instituto> institutosByNombre(@Argument String nombre) {
        return institutosRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // -----------------------
    // --- RESOLVERS RELACIONES ---
    // -----------------------

    @SchemaMapping(typeName = "Estudiante", field = "instituto")
    public Instituto instituto(Estudiante estudiante) {
        return estudiante.getInstituto();
    }

    @SchemaMapping(typeName = "Instituto", field = "estudiantes")
    public List<Estudiante> estudiantes(Instituto instituto) {
        return estudianteRepository.findByInstituto(instituto);
    }
}
