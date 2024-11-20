FROM openjdk:21-bookworm
WORKDIR /app
COPY build/libs/*.jar /app/news-service.jar
EXPOSE 64102
CMD ["java", "-jar", "news-service.jar"]