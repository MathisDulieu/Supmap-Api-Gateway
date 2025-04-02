FROM eclipse-temurin:21-jdk as build

WORKDIR /app

COPY settings.xml /root/.m2/settings.xml

COPY . .
RUN chmod +x ./mvnw

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /app/target/*.jar app.jar

RUN useradd runtime
USER runtime

ENTRYPOINT ["java", "-Dserver.port=8080", "-jar", "app.jar"]
