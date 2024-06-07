FROM eclipse-temurin:17-jdk-jammy AS base
WORKDIR /build
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/
COPY mashtframework ./mashtframework
COPY inkout ./inkout
COPY pom.xml .

#FROM base AS mashtframework-install
#WORKDIR /build
#RUN --mount=type=cache,target=/root/.m2 \
#    ./mvnw -pl mashtframework clean install -DskipTests


FROM base AS inkout-install
WORKDIR /build
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw clean install -DskipTests -e -X


FROM eclipse-temurin:17-jre-alpine AS inkout-target
RUN mkdir /opt/app
WORKDIR /opt/app
COPY --from=inkout-install /build/inkout/target/*-jar-with-dependencies.jar /opt/app/app.jar
EXPOSE 3000
CMD ["java", "-jar", "/opt/app/app.jar"]