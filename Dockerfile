FROM eclipse-temurin:25-jdk AS build

WORKDIR /workspace

RUN apt-get update \
    && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

COPY pom.xml ./
COPY application ./application
COPY .mvn ./.mvn
COPY mvnw ./

RUN chmod +x mvnw && ./mvnw -q -DskipTests -pl application/springboot -am clean package spring-boot:repackage

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /workspace/application/springboot/target/springboot-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
