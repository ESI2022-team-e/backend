FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} project.jar
ADD docker-utils/wait-for-it.sh wait-for-it.sh
EXPOSE 8080
CMD chmod +x wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "project:5432","--","java", "-jar", "/project.jar","--spring.datasource.url=jdbc:postgresql://localhost:5432/project"]