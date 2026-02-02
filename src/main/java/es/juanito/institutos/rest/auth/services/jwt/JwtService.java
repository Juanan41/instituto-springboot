package es.juanito.institutos.rest.auth.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public String extractUserName(String token) {
        return null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return true;
    }
}
