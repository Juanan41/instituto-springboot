package es.juanito.institutos.web.controllers;

import es.juanito.institutos.rest.estudiantes.models.Estudiante;
import es.juanito.institutos.rest.estudiantes.repositories.EstudianteRepository;
import es.juanito.institutos.rest.institutos.models.Instituto;
import es.juanito.institutos.rest.institutos.repositories.InstitutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/public")
@RequiredArgsConstructor
public class ZonaPublicaController {

    private final EstudianteRepository estudianteRepository;
    private final InstitutosRepository institutosRepository;

    // ===================================================
    // ✅ INDEX (LISTADOS + BUSCADOR + PAGINACIÓN)
    // ===================================================
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping({"", "/", "/index"})
    public String index(
            Model model,
            Locale locale,
            Authentication auth,

            // Institutos
            @RequestParam(defaultValue = "0") int pageInstitutos,
            @RequestParam(defaultValue = "4") int sizeInstitutos,

            // Estudiantes
            @RequestParam(defaultValue = "0") int pageEstudiantes,
            @RequestParam(defaultValue = "4") int sizeEstudiantes,

            // Buscadores
            @RequestParam(required = false) String qInstitutos,
            @RequestParam(required = false) String qEstudiantes,

            // ✅ idioma
            @RequestParam(required = false) String lang
    ) {
        // ✅ Pasar idioma al template
        model.addAttribute("lang", locale.getLanguage());

        String filtroInstitutos = (qInstitutos == null) ? "" : qInstitutos.trim();
        String filtroEstudiantes = (qEstudiantes == null) ? "" : qEstudiantes.trim();

        Pageable pageableInstitutos = PageRequest.of(pageInstitutos, sizeInstitutos, Sort.by("id").ascending());
        Pageable pageableEstudiantes = PageRequest.of(pageEstudiantes, sizeEstudiantes, Sort.by("id").ascending());

        Page<Instituto> institutosPage;
        Page<Estudiante> estudiantesPage;

        boolean esAdmin = auth != null &&
                auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        model.addAttribute("esAdmin", esAdmin);


        // ✅ FILTRO INSTITUTOS
        if (!filtroInstitutos.isBlank()) {
            institutosPage =
                    institutosRepository
                            .findByIsDeletedFalseAndNombreContainingIgnoreCaseOrIsDeletedFalseAndCiudadContainingIgnoreCaseOrIsDeletedFalseAndCodigoInstitutoContainingIgnoreCase(
                                    filtroInstitutos, filtroInstitutos, filtroInstitutos, pageableInstitutos
                            );
        } else {
            institutosPage = institutosRepository.findAll(pageableInstitutos);
        }

        // ✅ FILTRO ESTUDIANTES
        if (!filtroEstudiantes.isBlank()) {
            estudiantesPage =
                    estudianteRepository
                            .findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDniContainingIgnoreCase(
                                    filtroEstudiantes, filtroEstudiantes, filtroEstudiantes, pageableEstudiantes
                            );
        } else {
            estudiantesPage = estudianteRepository.findAll(pageableEstudiantes);
        }

        model.addAttribute("institutosPage", institutosPage);
        model.addAttribute("estudiantesPage", estudiantesPage);

        model.addAttribute("qInstitutos", filtroInstitutos);
        model.addAttribute("qEstudiantes", filtroEstudiantes);

        return "public/index";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Zona pública OK ✅";
    }

    // ===================================================
    // ✅ FORMULARIO CREAR INSTITUTO
    // ===================================================
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/institutos/nuevo")
    public String formNuevoInstituto(Model model, Locale locale) {
        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("instituto", new Instituto());
        return "formulario/instituto-form";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/institutos/nuevo")
    public String guardarNuevoInstituto(
            @ModelAttribute Instituto instituto,
            RedirectAttributes ra,
            @RequestParam(defaultValue = "es") String lang
    ) {
        if (instituto.getUuid() == null) {
            instituto.setUuid(java.util.UUID.randomUUID());
        }

        institutosRepository.save(instituto);

        ra.addFlashAttribute("msgOk", "✅ Instituto creado correctamente");
        return "redirect:/public?lang=" + lang;
    }





    // ===================================================
    // ✅ EDITAR INSTITUTO
    // ===================================================
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/institutos/editar/{id}")
    public String formEditarInstituto(@PathVariable Long id, Model model, Locale locale, RedirectAttributes ra) {
        model.addAttribute("lang", locale.getLanguage());

        return institutosRepository.findById(id)
                .map(inst -> {
                    model.addAttribute("instituto", inst);
                    return "formulario/instituto-form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("msgError", "❌ Instituto no encontrado (id=" + id + ")");
                    return "redirect:/public?lang=" + locale.getLanguage();
                });
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/institutos/editar/{id}")
    public String guardarEdicionInstituto(
            @PathVariable Long id,
            @ModelAttribute Instituto instituto,
            RedirectAttributes ra,
            @RequestParam(defaultValue = "es") String lang
    ) {
        // ✅ asegúrate de editar el mismo ID
        instituto.setId(id);

        // ✅ si no viene uuid, lo creamos
        if (instituto.getUuid() == null) {
            instituto.setUuid(java.util.UUID.randomUUID());
        }

        institutosRepository.save(instituto);

        ra.addFlashAttribute("msgOk", "✅ Instituto editado correctamente");
        return "redirect:/public/institutos/" + id + "?lang=" + lang;
    }

    // ===================================================
    // ✅ BORRAR INSTITUTO
    // ===================================================
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/institutos/borrar/{id}")
    public String borrarInstituto(@PathVariable Long id, RedirectAttributes ra,
                                  @RequestParam(defaultValue = "es") String lang) {
        institutosRepository.deleteById(id);
        ra.addFlashAttribute("msgOk", "✅ Instituto eliminado correctamente");
        return "redirect:/public?lang=" + lang;
    }

    // ===================================================
    // ✅ FORMULARIO CREAR ESTUDIANTE (INSTITUTO OPCIONAL)
    // ===================================================
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/estudiantes/nuevo")
    public String formNuevoEstudiante(Model model, Locale locale) {
        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("institutos", institutosRepository.findAll());
        return "formulario/estudiante-form";
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/estudiantes/nuevo")
    public String guardarNuevoEstudiante(
            @ModelAttribute Estudiante estudiante,
            @RequestParam(required = false) Long institutoId,
            RedirectAttributes ra,
            @RequestParam(defaultValue = "es") String lang
    ) {
        if (institutoId != null) {
            Instituto instituto = institutosRepository.findByIdConEstudiantes(institutoId)
                    .orElseThrow(() -> new RuntimeException("Instituto no encontrado"));
            estudiante.setInstituto(instituto);
        } else {
            estudiante.setInstituto(null);
        }

        if (estudiante.getDni() != null) {
            estudiante.setDni(estudiante.getDni().trim().replace(" ", "").toUpperCase());
        }

        if (estudiante.getUuid() == null) {
            estudiante.setUuid(java.util.UUID.randomUUID());
        }

        if (estudiante.getUsername() == null || estudiante.getUsername().isBlank()) {
            String nombre = (estudiante.getNombre() == null) ? "" : estudiante.getNombre().trim().toLowerCase();
            String apellidos = (estudiante.getApellidos() == null) ? "" : estudiante.getApellidos().trim().toLowerCase();

            String base = (nombre + "." + apellidos)
                    .replace(" ", "")
                    .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                    .replace("ñ", "n");

            if (base.isBlank()) base = "user";
            estudiante.setUsername(base + (int) (Math.random() * 1000));
        }

        if (estudiante.getPassword() == null || estudiante.getPassword().isBlank()) {
            estudiante.setPassword("1234");
        }

        // ======================
// AVATAR AUTOMÁTICO
// ======================
        String seed = estudiante.getUsername();

        String genero = estudiante.getGenero();

        String avatarUrl;

        if (genero != null && genero.equalsIgnoreCase("CHICA")) {
            avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed="
                    + seed + "&gender=female";
        } else if (genero != null && genero.equalsIgnoreCase("CHICO")) {
            avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed="
                    + seed + "&gender=male";
        } else {
            // si no eligió género → aleatorio
            avatarUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=" + seed;
        }

        estudiante.setAvatar(avatarUrl);



        estudianteRepository.save(estudiante);
        ra.addFlashAttribute("msgOk", "✅ Estudiante creado correctamente");
        return "redirect:/public/estudiantes/"
                + estudiante.getId()
                + "?lang=" + lang;

    }





    // ===================================================
    // ✅ EDITAR ESTUDIANTE (INSTITUTO OPCIONAL)
    // ===================================================
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/estudiantes/editar/{id}")
    public String formEditarEstudiante(@PathVariable Long id, Model model, Locale locale, RedirectAttributes ra) {
        model.addAttribute("lang", locale.getLanguage());

        return estudianteRepository.findById(id)
                .map(est -> {
                    model.addAttribute("estudiante", est);
                    model.addAttribute("institutos", institutosRepository.findAll());
                    return "formulario/estudiante-form";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("msgError", "❌ Estudiante no encontrado (id=" + id + ")");
                    return "redirect:/public?lang=" + locale.getLanguage();
                });
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/estudiantes/editar/{id}")
    public String guardarEdicionEstudiante(
            @PathVariable Long id,
            @ModelAttribute Estudiante estudiante,
            @RequestParam(required = false) Long institutoId,
            RedirectAttributes ra,
            @RequestParam(defaultValue = "es") String lang
    ) {
        estudiante.setId(id);

        if (institutoId != null) {
            Instituto instituto = institutosRepository.findByIdConEstudiantes(institutoId)
                    .orElseThrow(() -> new RuntimeException("Instituto no encontrado"));
            estudiante.setInstituto(instituto);
        } else {
            estudiante.setInstituto(null);
        }

        if (estudiante.getDni() != null) {
            estudiante.setDni(estudiante.getDni().trim().replace(" ", "").toUpperCase());
        }

        if (estudiante.getUuid() == null) {
            estudiante.setUuid(java.util.UUID.randomUUID());
        }

        if (estudiante.getPassword() == null || estudiante.getPassword().isBlank()) {
            estudiante.setPassword("1234");
        }

        estudianteRepository.save(estudiante);
        ra.addFlashAttribute("msgOk", "✅ Estudiante editado correctamente");
        return "redirect:/public/estudiantes/" + id + "?lang=" + lang;
    }
    // ===================================================
    // ✅ PERFIL
    // ===================================================

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/mi-perfil")
    public String miPerfil(Model model,
                           org.springframework.security.core.Authentication auth) {

        String username = auth.getName();

        Estudiante estudiante = estudianteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        model.addAttribute("estudiante", estudiante);

        return "detalles/estudiante-detalle";
    }


    // ===================================================
    // ✅ BORRAR ESTUDIANTE
    // ===================================================

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/estudiantes/borrar/{id}")
    public String borrarEstudiante(@PathVariable Long id, RedirectAttributes ra,
                                   @RequestParam(defaultValue = "es") String lang) {
        estudianteRepository.deleteById(id);
        ra.addFlashAttribute("msgOk", "✅ Estudiante eliminado correctamente");
        return "redirect:/public?lang=" + lang;
    }

    // ===================================================
    // ✅ DETALLES
    // ===================================================
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/institutos/{id}")
    public String verDetalleInstituto(@PathVariable Long id, Model model, Locale locale) {

        Instituto instituto = institutosRepository.findByIdConEstudiantes(id)
                .orElseThrow(() -> new RuntimeException("Instituto no encontrado"));

        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("instituto", instituto);

        return "detalles/instituto-detalle";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/estudiantes/{id}")
    public String verDetalleEstudiante(
            @PathVariable Long id,
            Model model,
            Locale locale,
            RedirectAttributes ra
    ) {
        model.addAttribute("lang", locale.getLanguage());

        return estudianteRepository.findByIdWithInstituto(id)
                .map(estudiante -> {
                    model.addAttribute("estudiante", estudiante);
                    return "detalles/estudiante-detalle";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("msgError", "❌ Estudiante no encontrado (id=" + id + ")");
                    return "redirect:/public?lang=" + locale.getLanguage();
                });
    }
}
