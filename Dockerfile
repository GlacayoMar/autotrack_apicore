# =========================================================================
# STAGE 1: Build the Application using Gradle
# =========================================================================
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copy gradle wrapper and configuration files first to cache dependencies
COPY --chown=gradle:gradle gradlew settings.gradle build.gradle ./
COPY --chown=gradle:gradle gradle ./gradle

# Copy the rest of the application source code
COPY --chown=gradle:gradle src ./src

# Compile and package the application into a fat bootJar without running tests
RUN ./gradlew bootJar --no-daemon -x test

# =========================================================================
# STAGE 2: Lightweight Production Runtime Environment
# =========================================================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root system user for security (Great for showing off DevOps best practices)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy ONLY the compiled .jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# -------------------------------------------------------------------------
# DEVOPS ENGINE OPTIMIZATIONS (For Render's 512MB RAM constraint)
# -------------------------------------------------------------------------
# -XX:+UseSerialGC: Minimizes internal memory footprint of Garbage Collection.
# -Xss256k: Lowers execution thread stack allocation sizes.
# -XX:MaxRAMPercentage: Reserves 25% of the 512MB box for the OS container to prevent OOM termination.
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xss256k -XX:MaxRAMPercentage=75.0"

# Expose port 8080 for web traffic
EXPOSE 8080

# Execute the application
ENTRYPOINT ["java", "-jar", "app.jar"]