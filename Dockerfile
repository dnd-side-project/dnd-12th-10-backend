FROM eclipse-temurin:17-jdk-alpine
COPY ./build/libs/*SNAPSHOT.jar reev.jar
ENTRYPOINT ["java", "-jar", "reev.jar"]