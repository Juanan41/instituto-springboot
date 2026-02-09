package es.juanito.institutos.config.auth.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LogoutSuccessListener {

    @EventListener
    public void onLogoutSuccess(LogoutSuccessEvent event) {

        SecurityStats.totalLogouts.incrementAndGet();

        System.out.println("🚪 LOGOUT");
        System.out.println("Usuario: " + event.getAuthentication().getName());
        System.out.println("Total logouts: " + SecurityStats.totalLogouts.get());
        System.out.println("--------------------------------");
    }
}

