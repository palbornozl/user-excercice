FROM gradle:6.6.1-jdk8 AS build
ARG DB_URL
ARG DB_USER
ARG DB_PASSWORD
ARG DB_DIALECT
ARG DB_DRIVER
ARG LOG_LEVEL

ENV DB_URL=$DB_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD
ENV DB_DIALECT=$DB_DIALECT
ENV DB_DRIVER=$DB_DRIVER
ENV LOG_LEVEL=$LOG_LEVEL

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN echo $DB_URL
RUN echo $DB_USER
RUN echo $DB_PASSWORD
RUN echo $DB_DIALECT
RUN echo $DB_DRIVER
RUN echo $LOG_LEVEL
RUN pwd
RUN gradle clean build
RUN ls -l /home/gradle/src/build/libs/

FROM adoptopenjdk:8-jre-openj9
RUN mkdir /app
RUN pwd
COPY --from=build /home/gradle/src/build/libs/user-1.0.0.jar /app/user-1.0.0.jar
WORKDIR /app
RUN java -version
CMD ["java", "-XX:+UseG1GC", "-jar", "user-1.0.0.jar", "10"]
