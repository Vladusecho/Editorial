FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 6767

ENTRYPOINT ["java", "-jar", "app.jar"]