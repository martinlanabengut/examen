# Etapa de construcción
FROM eclipse-temurin:11-jdk as build

COPY . /app
WORKDIR /app

RUN chmod +x mvnw
RUN ./mvnw package -DskipTests
RUN mv -f target/*.jar app.jar

# Etapa de ejecución
FROM eclipse-temurin:11-jre

# Establecer argumento y variable de entorno para el puerto
ARG PORT=8080
ENV PORT=${PORT}

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/app.jar .

# Crear un usuario para ejecutar la aplicación
RUN useradd runtime
USER runtime

# Exponer el puerto
EXPOSE ${PORT}

# Comando de entrada para ejecutar la aplicación
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "app.jar" ]
