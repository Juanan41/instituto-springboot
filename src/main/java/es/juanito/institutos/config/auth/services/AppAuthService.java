package es.juanito.institutos.config.auth.services;

import es.juanito.institutos.config.auth.models.AppUser;
import es.juanito.institutos.config.auth.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AppAuthService {

    private final AppUserRepository repo;
    private final PasswordEncoder passwordEncoder;

    /**
     * ✅ Registro persistente (se guarda en BD para siempre)
     */
    public void register(String username, String email, String password) {

        Set<String> roles;

        if (email.equalsIgnoreCase("email1983@gmail.com")) {
            roles = Set.of("ADMIN");
        } else {
            roles = Set.of("USER");
        }

        AppUser user = AppUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();

        repo.save(user);

    }


    /**
     * ✅ Login: true si usuario/email + password correctos
     */
    public boolean loginOk(String usernameOrEmail, String rawPassword) {

        final String key = usernameOrEmail.trim().toLowerCase();

        AppUser user = repo.findByUsername(key)
                .or(() -> repo.findByEmail(key))
                .orElse(null);

        if (user == null) return false;

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public AppUser getUserByUsernameOrEmail(String usernameOrEmail) {
        final String key = usernameOrEmail.trim().toLowerCase();

        return repo.findByUsername(key)
                .or(() -> repo.findByEmail(key))
                .orElse(null);
    }


}
