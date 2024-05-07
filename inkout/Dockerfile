FROM eclipse-temurin:17-jre-alpine

RUN mkdir /opt/app
COPY target/*-jar-with-dependencies.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]