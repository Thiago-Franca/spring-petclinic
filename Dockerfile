# syntax=docker/dockerfile:experimental
FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
RUN addgroup -S demo && adduser -S demo -G demo
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
RUN chown -R demo:demo /app
USER demo
ENTRYPOINT ["sh", "-c", "java -noverify -cp /app:/app/lib/* \
  org.springframework.samples.petclinic.PetClinicApplication ${0} ${@}"]
