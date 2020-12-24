FROM adoptopenjdk:11-jre-hotspot as builder

ARG DEPENDENCY
ARG MODULE
ARG PROFILE=docker

WORKDIR ${MODULE}
ARG JAR_FILE=${DEPENDENCY}/${MODULE}.jar

ONBUILD COPY ${JAR_FILE} application.jar
ONBUILD RUN java -Djarmode=layertools -jar application.jar extract
ONBUILD RUN  touch ./snapshot-dependencies/.blank

FROM openjdk:11.0.7-slim
ARG MODULE
WORKDIR ${MODULE}

ENV SPRING_PROFILES_ACTIVE ${PROFILE}

EXPOSE 8080  8761

ONBUILD COPY --from=builder ${MODULE}/dependencies/ ./
ONBUILD COPY --from=builder ${MODULE}/spring-boot-loader ./
ONBUILD COPY --from=builder ${MODULE}/snapshot-dependencies/ ./
ONBUILD COPY --from=builder ${MODULE}/application/ ./

#HEALTHCHECK --interval=5m --timeout=3s  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
