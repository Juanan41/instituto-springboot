package es.juanito.institutos.config.auth.services;

import es.juanito.institutos.config.auth.models.AppUser;
import es.juanito.institutos.config.auth.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthUsersServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String value)
            throws UsernameNotFoundException {

        AppUser user = appUserRepository
                .findByUsername(value)
                .or(() -> appUserRepository.findByEmail(value))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario o email no encontrado: " + value
                        )
                );

        return new User(
                user.getUsername(),   // 🔐 identidad real
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }
}
