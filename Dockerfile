FROM maven:3.8.3-openjdk AS build

WORKDIR /studiotg

COPY pom.xml ./
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk AS final
WORKDIR /studiotg
COPY --from=build /studiotg/target/*.jar /studiotg/studiotg.jar
ENTRYPOINT ["sh", "-c", "java -jar /studiotg/studiotg.jar"]