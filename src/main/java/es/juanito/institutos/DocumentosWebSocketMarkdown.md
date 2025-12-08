 Informe Técnico: Configuración y Pruebas del Servicio WebSocket en Spring Boot
 


1.  Introducción y Arquitectura del Proyecto
   Este informe documenta la implementación de la comunicación en tiempo real en el proyecto Spring Boot mediante el uso de WebSockets. El objetivo principal es establecer un mecanismo de notificación bidireccional y persistente que permita el broadcast instantáneo de eventos de la capa CRUD (Creación, Actualización, Eliminación) a todos los clientes suscritos.

El proyecto utiliza el protocolo STOMP (Simple Text Oriented Messaging Protocol) sobre WebSockets, una abstracción que facilita la gestión del envío y recepción de mensajes en la arquitectura de Spring.

2.  Configuración y Puesta en Marcha
   2.1. Requisitos y Dependencias
   Asegúrese de que el archivo pom.xml contenga la dependencia de Spring Boot Starter para WebSockets:

XML

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>


2.2. Configuración del Servidor y el Broker
La configuración central se gestiona en la clase WebSocketConfig.java para habilitar el servidor y definir las rutas de mensajería (Broker).

Archivo/Clase	Función	Configuración de Rutas
WebSocketConfig.java	Habilita el servidor de WebSockets y configura el broker (intermediario de mensajes).	Endpoint de Conexión: /ws (Ejemplo: ws://localhost:8080/ws)
Prefijo del Broker: /topic (Para mensajes de broadcast del servidor a los clientes)
Prefijo de la Aplicación: /app (Para mensajes que van del cliente al servidor)

Exportar a Hojas de cálculo

2.3. Disparo de Notificaciones (Publisher)
Para enviar mensajes en tiempo real desde la capa de servicio (por ejemplo, después de guardar un Instituto), se utiliza la clase SimpMessagingTemplate.

Implementación en el Servicio (Ejemplo: InstitutoServiceImpl.java)

Java

// A. Inyección de la dependencia:
@Autowired
private SimpMessagingTemplate messagingTemplate;

// B. Envío del mensaje después de la operación (Guardar/Eliminar):
// Dentro del método create/save:
messagingTemplate.convertAndSend(
"/topic/institutos", // Canal de destino para suscriptores
mapper.toNotificationResponse(savedEntity, "CREATE") // Payload con el mensaje JSON
);



3.  Pruebas de Funcionamiento y Verificación
   Las pruebas aseguran que la comunicación bidireccional se establece correctamente.

3.1. Pruebas de Conexión y Suscripción (Cliente)
Para simular un cliente suscrito, se requiere una herramienta que soporte el protocolo STOMP (ej., Postman, extensiones de navegador, o un cliente JavaScript).

Paso	Acción del Cliente	Propósito
A. Conexión	Conectar a ws://localhost:8080/ws	Iniciar el handshake WebSocket.
B. Suscripción	Enviar el comando SUBSCRIBE /topic/institutos	El cliente queda registrado para recibir todos los mensajes de ese canal.
C. Verificación	Enviar una petición REST (POST a /api/v1/institutos)	Se espera que el cliente suscrito reciba instantáneamente el mensaje JSON con la entidad recién creada.

Exportar a Hojas de cálculo




4.  Análisis, Valoraciones y Propuestas de Mejora
   4.1. Valoración de la Integración
   La elección de Spring Boot con STOMP es altamente efectiva. El framework oculta la complejidad del manejo de sockets y threads, permitiendo al desarrollador centrarse en la lógica de la aplicación y en los canales de comunicación (/topic/). Esto simplifica drásticamente la implementación de la arquitectura Publisher/Subscriber.

4.2. Alternativas al Stack y Consideraciones
Alternativa	Descripción y Justificación	Contexto de Uso
Broker Externo (Kafka/RabbitMQ)	El Simple Broker interno de Spring es suficiente para desarrollo local. En producción, un broker dedicado es necesario para manejar la escalabilidad horizontal (múltiples instancias del servidor) y asegurar la fiabilidad de los mensajes.	Entornos productivos de alta concurrencia y despliegues en clúster.
Server-Sent Events (SSE)	Un protocolo más sencillo de implementar en Spring que WebSockets si la comunicación es unidireccional (solo servidor envía al cliente), como en el caso de notificaciones de broadcast.	Cuando no se requiere la funcionalidad de que el cliente envíe mensajes de vuelta al servidor (ej., chat).

Exportar a Hojas de cálculo

4.3. Propuestas de Despliegue en Entorno Real
Seguridad (WSS): En un entorno real (VPS), es mandatorio migrar de ws:// a wss:// (WebSocket Secure) utilizando un certificado SSL (ej., Let's Encrypt). La comunicación sin cifrar no es aceptable para datos sensibles.

Configuración de Proxy Inverso (Nginx): Si se utiliza Nginx como proxy delante de Spring Boot, debe configurarse para que gestione correctamente el protocolo WebSocket. Esto implica añadir las cabeceras Upgrade y Connection: Upgrade.

Implementación de Autenticación: Se recomienda integrar el handshake WebSocket con el sistema de seguridad (ej., JWT o Spring Security) para asegurar que solo los usuarios autenticados puedan suscribirse a canales específicos (/topic/).