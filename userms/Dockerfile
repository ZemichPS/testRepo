FROM openjdk:21-bookworm
WORKDIR /app
COPY build/libs/*.jar /app/user-service.jar
EXPOSE 58231
CMD ["java", "-jar", "user-service.jar"]