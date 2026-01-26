package es.juanito.institutos.web.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;

/**
 * ControllerAdvice para exponer atributos globales a TODAS las vistas (Pebble).
 * Todo lo que pongas aquí se podrá usar directamente en los templates.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    // Lee el nombre de la app desde application.properties:
    // spring.application.name=Institutos API Rest Spring Boot
    @Value("${spring.application.name}")
    private String appName;

    /**
     * Disponible en todas las vistas como: {{ appName }}
     */
    @ModelAttribute("appName")
    public String getAppName() {
        return appName;
    }

    /**
     * Disponible en todas las vistas como: {{ currentYear }}
     */
    @ModelAttribute("currentYear")
    public int getCurrentYear() {
        return LocalDate.now().getYear();
    }
    @ModelAttribute("appDescription")
    public String getAppDescription() {
        return "Gestión de Institutos y Estudiantes";
    }

}


