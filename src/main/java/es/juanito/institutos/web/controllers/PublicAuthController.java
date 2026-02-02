package es.juanito.institutos.web.controllers;

import es.juanito.institutos.config.auth.services.AppAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping("/public/auth")
public class PublicAuthController {

    private final AppAuthService authService;



    // ✅ Página principal con 2 opciones: login / registro
    @GetMapping({"", "/", "/index"})
    public String authIndex(Model model, Locale locale) {
        model.addAttribute("lang", locale.getLanguage());
        return "auth/auth-index";
    }

    // ✅ FORM LOGIN
    @GetMapping("/login")
    public String loginForm(Model model, Locale locale) {
        model.addAttribute("lang", locale.getLanguage());
        return "auth/login";
    }


    // ✅ FORM REGISTER
    @GetMapping("/register")
    public String registerForm(Model model, Locale locale) {
        model.addAttribute("lang", locale.getLanguage());
        return "auth/register";
    }

    // ✅ PROCESAR REGISTER
    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String password2,
                             RedirectAttributes ra,
                             @RequestParam(defaultValue = "es") String lang) {

        if (!password.equals(password2)) {
            ra.addFlashAttribute("msgError", "❌ Las contraseñas no coinciden");
            return "redirect:/public/auth/register?lang=" + lang;
        }

        try {
            authService.register(username, email, password);
        } catch (Exception e) {
            ra.addFlashAttribute("msgError", "❌ " + e.getMessage());
            return "redirect:/public/auth/register?lang=" + lang;
        }

        ra.addFlashAttribute("msgOk", "✅ Cuenta creada. Ahora inicia sesión.");
        return "redirect:/public/auth/login?lang=" + lang;
    }


}
