FROM openjdk:17
EXPOSE 8081
MAINTAINER sam
COPY target/vinyl-record-api.jar app.jar
ENTRYPOINT ["ja:va","-jar","/app.jar"]