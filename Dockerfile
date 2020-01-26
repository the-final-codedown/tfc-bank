FROM openjdk:8-jre-alpine

EXPOSE 8081

RUN mkdir /app
WORKDIR /app
ADD target/tfc-bank-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT java -jar app.jar