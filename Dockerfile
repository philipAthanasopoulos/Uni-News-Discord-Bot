# Start with a base image containing Ubuntu
FROM ubuntu:latest

# Install OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean;

# Add the application's jar to the container
ADD . /app/

# Find the path to the jar file and store it in a variable
RUN JAR_FILE=$(find /app -name 'WebScraper.jar') && \
    echo "JAR_FILE=$JAR_FILE" > /env_vars

# Load the environment variables
RUN echo "source /env_vars" >> ~/.bashrc

# Run the jar file
CMD /bin/bash -c 'source ~/.bashrc && java -jar $JAR_FILE'