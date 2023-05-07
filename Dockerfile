FROM openjdk:8-alpine

COPY target/uberjar/park.jar /park/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/park/app.jar"]
