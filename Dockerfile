# --- Build Stage ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- Runtime Stage ---
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=build /app/target/bookstore-0.0.1-SNAPSHOT.jar bookstore.jar

# Expose the port your app runs on
EXPOSE 8081

# Optional label
LABEL authors="engineerfred"

# Run the jar
ENTRYPOINT ["java", "-jar", "bookstore.jar"]
