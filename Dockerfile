FROM amazoncorretto:17-alpine3.17-jdk
COPY ./target/receipt-processor-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]