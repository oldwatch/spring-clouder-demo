FROM adoptopenjdk:11-jre-hotspot as builder

ARG DEPENDENCY
ARG JAR

WORKDIR application
ARG JAR_FILE=${DEPENDENCY}/${JAR}
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
RUN  touch ./snapshot-dependencies/.blank
FROM openjdk:11.0.7-slim
WORKDIR app

ARG PROFILE=docker

ENV E_PROFILE ${PROFILE}

EXPOSE 8080

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

#COPY  ${DEPENDENCY}/${JAR}  /app/${JAR}

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
#ENTRYPOINT java  "-Dspring.profiles.active=$E_PROFILE" -jar "/app/application.jar"