package es.juanito.institutos.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${api.version}")
    private String apiVersion;

    // Configuración de JWT Bearer
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("API REST Instituto - Gestión de Estudiantes")
                                .version(apiVersion)
                                .description("API REST para la gestión de Institutos y Estudiantes (Spring Boot + JWT).")
                                .termsOfService("https://example.com/terms")
                                .license(
                                        new License()
                                                .name("MIT")
                                                .url("https://opensource.org/licenses/MIT")
                                )
                                .contact(
                                        new Contact()
                                                .name("Juanito / Instituto API")
                                                .email("tuemail@ejemplo.com")
                                                .url("https://github.com/tuusuario")
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Repositorio del Proyecto")
                                .url("https://github.com/tuusuario/Instituto")
                )
                // Seguridad JWT en Swagger
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(
                        new Components().addSecuritySchemes(
                                "Bearer Authentication",
                                createAPIKeyScheme()
                        )
                );
    }

    /**
     * Grupo principal: rutas de Estudiantes
     * Ajusta la ruta según tus endpoints reales
     */
    @Bean
    public GroupedOpenApi estudiantesApi() {
        return GroupedOpenApi.builder()
                .group("estudiantes")
                .pathsToMatch("/api/" + apiVersion + "/estudiantes/**")
                .displayName("API Estudiantes")
                .build();
    }

    /**
     * Grupo secundario: rutas de Institutos
     */
    @Bean
    public GroupedOpenApi institutosApi() {
        return GroupedOpenApi.builder()
                .group("institutos")
                .pathsToMatch("/api/" + apiVersion + "/institutos/**")
                .displayName("API Institutos")
                .build();
    }

    /**
     * Grupo opcional: auth / login / registro
     * Si tus rutas no son estas, dímelas y te las cambio.
     */
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch(
                        "/api/" + apiVersion + "/auth/**",
                        "/api/" + apiVersion + "/login/**"
                )
                .displayName("API Auth")
                .build();
    }
}

