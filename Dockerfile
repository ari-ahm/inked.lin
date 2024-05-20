FROM eclipse-temurin:17-jdk-jammy AS base
WORKDIR /build
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/
COPY mashtframework ./mashtframework
COPY inkout ./inkout
COPY pom.xml .

FROM base AS mashtframework-install
WORKDIR /build
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -pl mashtframework clean install -DskipTests