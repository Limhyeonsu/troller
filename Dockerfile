FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} troller.jar
ENTRYPOINT ["java","-jar","/troller.jar"]
