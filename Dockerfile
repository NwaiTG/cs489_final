FROM amazoncorretto:23-alpine
LABEL authors="nawi"

WORKDIR /app

COPY build/libs/dentalsys-0.0.1-SNAPSHOT.jar /app

COPY src/main/resources/application.properties application.properties
ENTRYPOINT ["java", "-jar", "dentalsys-0.0.1-SNAPSHOT.jar", "--spring.config.location=file:application.properties"]

#ENTRYPOINT ["java", "-jar", "dentalsys-0.0.1-SNAPSHOT.jar"]