FROM eclipse-temurin:21-jdk as build

ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD

ENV NEXUS_USERNAME=${NEXUS_USERNAME}
ENV NEXUS_PASSWORD=${NEXUS_PASSWORD}

WORKDIR /app

COPY settings.xml /root/.m2/settings.xml

COPY . .
RUN chmod +x ./mvnw

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre

ARG PORT=8080
ENV PORT=${PORT}

COPY --from=build /app/target/*.jar app.jar

RUN useradd runtime
USER runtime

ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]