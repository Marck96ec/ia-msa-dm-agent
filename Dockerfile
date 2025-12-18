FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/src
COPY . .
RUN chmod +x gradlew && ./gradlew clean build -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
