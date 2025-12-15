package es.juanito.institutos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InstitutoApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(InstitutoApplication.class, args);
        System.out.println("JWT Secret: " + ctx.getEnvironment().getProperty("jwt.secret.key"));
        System.out.println("JWT Expiration: " + ctx.getEnvironment().getProperty("jwt.expiration.time"));
    }
}
