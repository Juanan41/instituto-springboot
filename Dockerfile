# Java 21 (igual que tu pom.xml)
FROM eclipse-temurin:21-jdk

# Directorio de trabajo
WORKDIR /app

# Copiar cualquier JAR generado por Maven
COPY target/*.jar app.jar

# Puerto de la aplicación
EXPOSE 8080

# Arranque de Spring Boot
ENTRYPOINT ["java","-jar","/app/app.jar"]
