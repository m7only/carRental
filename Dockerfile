FROM amazoncorretto:17-alpine-jdk as builder
MAINTAINER m7only
WORKDIR /app
COPY . /app/.
RUN ./mvnw -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM amazoncorretto:17-alpine-jdk
MAINTAINER m7only
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]