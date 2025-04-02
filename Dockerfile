FROM eclipse-temurin:21-jdk as build

WORKDIR /app

COPY . .
RUN chmod +x ./mvnw

RUN curl -u $NEXUS_USERNAME:$NEXUS_PASSWORD -I https://nexus3-production.up.railway.app/repository/maven-releases/

RUN ./mvnw dependency:purge-local-repository
RUN ./mvnw clean package -U -DskipTests

RUN ./mvnw package -DskipTests -X

FROM eclipse-temurin:21-jre

ARG PORT=8080
ENV PORT=${PORT}

COPY --from=build /app/target/*.jar app.jar

RUN useradd runtime
USER runtime

ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]