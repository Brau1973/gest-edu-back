# Usar una imagen base de Gradle con JDK 21 para construir el jar
FROM gradle:8.7.0-jdk-21-and-22 AS builder

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar todo el contenido del directorio actual (donde se encuentra el Dockerfile) al directorio /app dentro del contenedor
COPY . /app

# Ejecutar el comando Gradle para construir el proyecto y generar el archivo .jar
RUN gradle build --no-daemon

# Usar una imagen base de OpenJDK 21 en su versión ligera (slim) para el runtime
FROM openjdk:21-slim

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar el archivo .jar generado en la fase de construcción al directorio /app en la fase de runtime
COPY --from=builder /app/build/libs/gest-edu-0.0.1-SNAPSHOT.jar /app/gest-edu-back.jar

# Exponer el puerto 8080 para permitir la comunicación con la aplicación
EXPOSE 8080

# Definir el comando que se ejecutará cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "/app/gest-edu-back.jar"]
