# Start with a base image containing Ubuntu
FROM ubuntu:latest

# Install OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean;

# Add the application's jar to the container
ADD out/artifacts/WebScraper_jar/WebScraper.jar app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]