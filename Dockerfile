# syntax=docker/dockerfile:experimental
FROM adoptopenjdk:14-jdk-hotspot as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

ARG START=org.springframework.samples.petclinic.PetClinicApplication

RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests
RUN mkdir -p target/lib && cp $(java -jar target/*.jar --thin.classpath | tr : '\n') target/lib
RUN echo "#!/bin/sh" > list.sh && \
    echo "java -Xshare:off -XX:DumpLoadedClassList=app.classlist -cp 'target/lib/*' ${START} &" >> list.sh && \
    echo "while ! curl localhost:8080 >/dev/null 2>&1; do echo Waiting; sleep 1; done" >> list.sh && \
    chmod +x list.sh && \
    ./list.sh && \
    rm list.sh

RUN java -Xshare:dump -XX:SharedClassListFile=app.classlist -XX:SharedArchiveFile=app.jsa -cp 'target/lib/*' ${START}

FROM adoptopenjdk:14-jdk-hotspot
VOLUME /tmp
ARG APP=/workspace/app
WORKDIR /app
COPY --from=build ${APP}/target/lib target/lib
COPY --from=build ${APP}/app.jsa target/
ENTRYPOINT ["java", "-Xmx128m", "-Xshare:on", "-XX:SharedArchiveFile=target/app.jsa", "-Dspring.config.location=classpath:/application.properties", "-Dspring.main.lazy-initialization=true", \
			"-Dspring.data.jpa.repositories.bootstrap-mode=lazy", "-Dspring.cache.type=none", "-Dspring.jmx.enabled=false", "-cp","target/lib/*","org.springframework.samples.petclinic.PetClinicApplication"]
