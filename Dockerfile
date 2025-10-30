# Build stage
FROM amazoncorretto:17-alpine AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Run stage
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/tads_rivero-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
