FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
RUN apk add --no-cache tzdata
ENV TZ=Asia/Kolkata
ENTRYPOINT ["java","-jar","/app.jar"]