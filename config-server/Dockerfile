FROM openjdk:21-bookworm
WORKDIR /app
COPY build/libs/*.jar /app/config-server.jar
EXPOSE 8888
CMD ["java", "-jar", "config-server.jar"]