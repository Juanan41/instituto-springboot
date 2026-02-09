package es.juanito.institutos.config.auth.listeners;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessListener implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        SecurityStats.totalLogins.incrementAndGet();

        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            SecurityStats.adminLogins.incrementAndGet();
        } else {
            SecurityStats.userLogins.incrementAndGet();
        }

        System.out.println("🔐 LOGIN OK: " + username);
        System.out.println("Total logins: " + SecurityStats.totalLogins.get());
        System.out.println("Admins: " + SecurityStats.adminLogins.get());
        System.out.println("Users: " + SecurityStats.userLogins.get());
        System.out.println("--------------------------------");

        // redirección normal
        response.sendRedirect("/public");
    }
}

