FROM openjdk:20
LABEL authors="irinaamsn"

WORKDIR /app

COPY build/libs/todolist-0.0.1-SNAPSHOT.jar todolist.jar

CMD ["java", "-jar", "todolist.jar"]