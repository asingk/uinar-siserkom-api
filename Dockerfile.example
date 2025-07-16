FROM eclipse-temurin:17-jdk-alpine
LABEL authors="asingk"

RUN apk add --no-cache tzdata
ENV TZ=Asia/Jakarta
RUN apk add --no-cache openssh

COPY fonts/*.ttf ./
RUN mkdir -p /usr/share/fonts/truetype/
RUN install -m644 *.ttf /usr/share/fonts/truetype/
RUN rm ./*.ttf

RUN addgroup -S asingk && adduser -S asingk -G asingk
USER asingk
RUN mkdir -p ~/.ssh
RUN touch ~/.ssh/known_hosts
RUN	ssh-keyscan -H 192.168.176.26 >> ~/.ssh/known_hosts

#ARG JAR_FILE=build/libs/*SNAPSHOT.jar
ARG JAR_FILE=build/libs/*RELEASE.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
