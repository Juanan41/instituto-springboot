package es.juanito.institutos.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Landing page
    @GetMapping("/")
    public String landing() {
        return "public/landing";
    }

}
