package es.juanito.institutos.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/index"})
    public String home() {
        // Redirige a la zona pública
        return "redirect:/public";
    }
}
