#FROM gradle:7.1.0-jdk16 AS build
#COPY --chown=gradle:gradle . /home/gradle
#WORKDIR /home/gradle
#RUN gradle build --no-daemon


FROM openjdk:16-alpine3.13
ENV TZ Europe/Moscowв
COPY build/libs/Rental-0.0.1-SNAPSHOT.jar Rental-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Rental-0.0.1-SNAPSHOT.jar"]