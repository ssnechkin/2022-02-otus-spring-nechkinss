FROM maven:3.8.6-jdk-11
ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

ADD pom.xml $PROJECT_DIR
RUN mvn dependency:resolve -q

ADD src/ $PROJECT_DIR/src
RUN mvn install -DskipTests -q

FROM openjdk:11
ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=0 $PROJECT_DIR/target/homework_18-0.0.1.jar $PROJECT_DIR/

EXPOSE 9092
ENTRYPOINT ["java", "-jar", "/opt/project/homework_18-0.0.1.jar"]

RUN ln -sf ./booklib.log $PROJECT_DIR/book_lib.log
