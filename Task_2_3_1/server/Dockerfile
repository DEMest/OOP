FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /build

# Copy gradle wrapper and server sources
COPY gradlew ./
COPY gradle/ gradle/
COPY server/ server/

# Create minimal settings for server-only build
RUN printf 'rootProject.name = "snake-server"\ninclude "server"\n' > settings.gradle
RUN chmod +x gradlew
RUN ./gradlew :server:shadowJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/server/build/libs/server-1.0-SNAPSHOT.jar app.jar
EXPOSE 6769
ENV PORT=6769
CMD ["java", "-jar", "app.jar"]
