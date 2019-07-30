FROM openjdk:11-jdk
VOLUME /tmp
EXPOSE 8081
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.config.location=file:/config/application.properties"]