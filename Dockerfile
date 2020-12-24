FROM openjdk:11-jdk-buster

ARG PROFILE=docker

WORKDIR app

ENV SPRING_PROFILES_ACTIVE ${PROFILE}

EXPOSE 8080  8761

COPY ["./target/dependencies/", \ 
      "./target/spring-boot-loader/",  \  
      "./target/snapshot-dependencies/", \
      "./target/application/", "./" ]

HEALTHCHECK --interval=5m --timeout=3s  CMD STATUS= `curl -f --silent  http://localhost:8080/actuator/health|grep "UP"`; \
    if [ -n $STATUS ];then exit 0; else exit 1; fi;

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
