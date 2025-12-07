package es.juanito.institutos.config.websockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable, WebSocketSender {
    private final String entity; // Entidad que se notifica

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    public WebSocketHandler(String entity) {
        this.entity = entity;
    }

    /**
     * Cuando se establece la conexión con el servidor
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Conexión establecida con el servidor para la entidad: {}", entity);
        log.info("Sesión: {}", session);
        sessions.add(session);
        // Mensaje adaptado
        TextMessage message = new TextMessage("Updates Web socket: " + entity + " - (App Instituto)");
        log.info("Servidor envía: {}", message.getPayload());
        session.sendMessage(message);
    }

    /**
     * Cuando se cierra la conexión con el servidor
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Conexión cerrada con el servidor para {}: {}", entity, status);
        sessions.remove(session);
    }

    /**
     * Envía un mensaje a todos los clientes conectados
     */
    @Override
    public void sendMessage(String message) throws IOException {
        log.info("Enviar mensaje de cambios en la entidad: {} : {} ", entity, message);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                log.info("Servidor WS envía: {}", message);
                session.sendMessage(new TextMessage(message));
            }
        }
    }

    /**
     * Envía mensajes periódicos (Heartbeat)
     */
    @Scheduled(fixedRate = 10000) // Cambiado a 10s para evitar logs excesivos, pero mantenemos la funcionalidad
    @Override
    public void sendPeriodicMessages() throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                String broadcast = "server periodic message " + entity + " " + LocalTime.now();
                log.debug("Server sends heartbeat: {}", broadcast);
                session.sendMessage(new TextMessage(broadcast));
            }
        }
    }

    /**
     * Maneja los mensajes de texto que le llegan al servidor
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Mensaje de cliente recibido en {}: {}", entity, message.getPayload());
    }

    /**
     * Maneja los errores de transporte
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Error de transporte con el servidor: {}", exception.getMessage());
    }

    /**
     * Devuelve los subprotocolos que soporta el servidor
     */
    @Override
    public List<String> getSubProtocols() {
        return List.of("subprotocol.instituto.websocket"); // Adaptado al proyecto
    }
}