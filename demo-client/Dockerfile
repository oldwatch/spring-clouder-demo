FROM openjdk:11.0.7-slim

VOLUME /tmp

#ARG DEPENDENCY=target
ARG DEPENDENCY=target
ARG JAR
ARG PROFILE=docker

#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring

#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
COPY  ${DEPENDENCY}/${JAR}  /app/${JAR}

ENV E_PROFILE ${PROFILE}
ENV E_JAR  ${JAR}

EXPOSE 8080


ENTRYPOINT java  "-Dspring.profiles.active=$E_PROFILE" -jar "/app/$E_JAR"