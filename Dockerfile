# Stage 2
FROM openjdk:11.0.16-jdk

WORKDIR /app

COPY /gateway/target/gateway*.jar .

RUN mv ./gateway*.jar  ./gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gateway.jar.jar"]