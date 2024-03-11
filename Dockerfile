# Start with a base image containing Ubuntu
FROM ubuntu:latest

# Install OpenJDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean;

# Change the working directory
WORKDIR /home/user/.local/tmp/buildkit-mount1801562603/

# Execute ls -l
RUN ls -l

# Add the application's jar to the container
