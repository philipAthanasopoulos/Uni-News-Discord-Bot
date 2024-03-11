# Start with a base image containing Ubuntu
FROM ubuntu:latest


# Change the working directory
WORKDIR /home/user/.local/tmp/buildkit-mount1801562603/

# Execute ls -l
RUN echo "Listing directory contents:" && \
    ls -l && \
    echo "End of directory listing"

# Add the application's jar to the container
