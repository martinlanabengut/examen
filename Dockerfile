# Etapa 1: Construcción
FROM eclipse-temurin:11-jdk as build

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar el contenido del proyecto al contenedor
COPY . .

# Dar permisos de ejecución a Maven Wrapper y construir la aplicación
RUN chmod +x mvnw
RUN ./mvnw package

# Mover el JAR generado a la ubicación /app/app.jar
RUN mv target/*.jar app.jar

# Etapa 2: Ejecución
FROM eclipse-temurin:11-jre

# Establecer el puerto en el que la aplicación escuchará
ARG PORT=8080
ENV PORT=${PORT}

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/app.jar .

# Crear un usuario para ejecutar la aplicación
RUN useradd -m runtime
USER runtime

# Exponer el puerto
EXPOSE ${PORT}

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
