# Dockerfile

# jdk17 Image Start
FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/moonspoon-0.0.1-SNAPSHOT.jar

# jar 파일 복제
COPY ${JAR_FILE} app.jar

# 인자 설정 부분과 jar 파일 복제 부분 합쳐서 진행해도 무방
#COPY build/libs/*.jar app.jar
EXPOSE 8080

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]