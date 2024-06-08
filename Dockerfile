# Usar una imagen base de Java 21
FROM openjdk:21-slim

# Copiar el archivo jar de la aplicación al contenedor
ARG JAR_FILE=build/libs/gest-edu-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} gest-edu-back.jar

# Exponer el puerto 8080 para permitir la comunicación con la aplicación
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java","-jar","/gest-edu-back.jar"]