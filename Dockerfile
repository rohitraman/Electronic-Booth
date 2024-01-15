FROM eclipse-temurin:21-jdk-alpine

RUN apk update
RUN apk upgrade
RUN apk add ca-certificates && update-ca-certificates
# Change TimeZone
RUN apk add --no-cache tzdata
ENV TZ=Asia/Kolkata
# Clean APK cache
RUN rm -rf /var/cache/apk/*
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]