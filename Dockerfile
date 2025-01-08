FROM openjdk:17

WORKDIR application

COPY target/BookServiceAPI-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-jar", "BookServiceAPI-0.0.1-SNAPSHOT.jar"]

