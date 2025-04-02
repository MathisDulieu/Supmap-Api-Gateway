FROM eclipse-temurin:21-jdk as build

WORKDIR /app

COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre

ARG PORT=8080
ENV PORT=${PORT}

COPY --from=build /app/target/*.jar app.jar
COPY nginx.conf /etc/nginx/nginx.conf

RUN apt-get update && apt-get install -y nginx
RUN useradd runtime
USER runtime

ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]