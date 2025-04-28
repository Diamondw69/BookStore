FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /workspace

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw \
 && ./mvnw dependency:go-offline -B


COPY src src
RUN ./mvnw clean package -DskipTests -B


FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

EXPOSE 8080

ARG JAR_FILE=target/*.jar
COPY --from=builder /workspace/${JAR_FILE} app.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
