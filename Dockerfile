# Builder stage
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# 1. Copy only pom.xml first
COPY pom.xml .

# 2. Download dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# 3. Now copy the actual source code
COPY src ./src

# 4. Build the application
RUN mvn clean install -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar
COPY --from=builder /app/target/Chat-Storage-1.0-SNAPSHOT.jar app.jar

# JVM arguments
ENTRYPOINT ["java", "-Duser.timezone=Asia/Kolkata", "-jar", "/app/app.jar"]

