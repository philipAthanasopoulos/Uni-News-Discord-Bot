FROM openjdk:21

WORKDIR /usr/src

COPY out/artifacts/WebScraper_jar/WebScraper.jar /usr/src/WebScraper.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "WebScraper.jar"]