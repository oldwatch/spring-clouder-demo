FROM openjdk:11.0.7-slim

VOLUME /tmp

ARG DEPENDENCY
ARG JAR
ARG PROFILE=docker

ENV E_PROFILE ${PROFILE}
ENV E_JAR  ${JAR}

EXPOSE 8080

COPY  ${DEPENDENCY}/${JAR}  /app/${JAR}

ENTRYPOINT java  "-Dspring.profiles.active=$E_PROFILE" -jar "/app/$E_JAR"