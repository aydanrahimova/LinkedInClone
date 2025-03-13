FROM openjdk:23-jdk
WORKDIR /app
COPY build/libs/LinkedInClone-0.0.1.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
