FROM openjdk:22-jdk
COPY out/artifacts/WebScraper_jar/WebScraper.jar bot.jar
ENTRYPOINT ["java", "-jar", "/bot.jar"]