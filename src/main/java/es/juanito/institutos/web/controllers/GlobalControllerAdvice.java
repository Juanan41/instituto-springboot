package es.juanito.institutos.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("loggedUser")
    public String loggedUser() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth != null &&
                auth.isAuthenticated() &&
                !auth.getPrincipal().equals("anonymousUser")) {

            return auth.getName();
        }

        return null;
    }

    @ModelAttribute("loggedRoles")
    public Collection<? extends GrantedAuthority> loggedRoles() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth != null &&
                auth.isAuthenticated() &&
                !auth.getPrincipal().equals("anonymousUser")) {

            return auth.getAuthorities();
        }

        return null;
    }
}
