FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} volunteerapp.jar

ENTRYPOINT ["java", "-jar", "/volunteerapp.jar"]