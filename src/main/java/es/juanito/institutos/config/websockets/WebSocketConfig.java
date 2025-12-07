// src/main/java/es/juanito/institutos/config/websockets/WebSocketConfig.java

package es.juanito.institutos.config.websockets; // <-- PAQUETE CORREGIDO

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling; // Necesario para el @Scheduled en el Handler
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@EnableScheduling // Habilita el envío periódico de mensajes
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${api.version}")
    private String apiVersion;

    /**
     * Registra los WebSocket Handlers para Estudiantes e Institutos.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        String baseUri = "/ws/" + apiVersion;

        // 1. Registro para INSTITUTOS (Análogo a /tarjetas)
        registry.addHandler(webSocketInstitutosHandler(), baseUri + "/institutos")
                .setAllowedOrigins("*");

        // 2. Registro para ESTUDIANTES
        registry.addHandler(webSocketEstudiantesHandler(), baseUri + "/estudiantes")
                .setAllowedOrigins("*");
    }

    // --- Definición de Beans Handlers ---

    @Bean
    public WebSocketHandler webSocketInstitutosHandler() {
        // Usamos la clase concreta WebSocketHandler
        return new WebSocketHandler("Institutos");
    }

    @Bean
    public WebSocketHandler webSocketEstudiantesHandler() {
        return new WebSocketHandler("Estudiantes");
    }
}