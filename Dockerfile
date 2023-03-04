FROM gradle:6.9.4-jdk8 AS build
ARG DB_URL
ARG DB_USER
ARG DB_PASSWORD
ARG DB_DIALECT
ARG DB_DRIVER
ARG LOG_LEVEL
ARG LOG_LEVEL_SQL

ENV DB_URL=$DB_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD
ENV DB_DIALECT=$DB_DIALECT
ENV DB_DRIVER=$DB_DRIVER
ENV LOG_LEVEL=$LOG_LEVEL
ENV LOG_LEVEL_SQL=$LOG_LEVEL_SQL

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build -x test
RUN ls -l /home/gradle/src/build/libs/

FROM adoptopenjdk:8-jre-openj9
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/user-1.0.0.jar /app/user-1.0.0.jar
WORKDIR /app
RUN java -version
CMD ["java", "-XX:+UseG1GC", "-jar", "user-1.0.0.jar", "10"]
