# Start with a base image for Maven
FROM maven:3.8.5-openjdk-17 AS build

# Copy your project files into the Docker image
COPY . .

# Use Maven to build your project
RUN mvn clean package -DskipTests

# Start a new stage with an OpenJDK base image
FROM openjdk:17.0.1-jdk-slim

# Copy the .jar file from the first stage into the second stage
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar

# Set the ENTRYPOINT to run your .jar file
ENTRYPOINT ["java","-jar","demo.jar"]