FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src src
RUN mvn -q -DskipTests package
FROM eclipse-temurin:17-jre
WORKDIR /app
RUN useradd --system --uid 10001 medialert
COPY --from=build /app/target/*.jar app.jar
USER 10001
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/app.jar"]
