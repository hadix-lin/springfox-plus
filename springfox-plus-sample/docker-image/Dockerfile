FROM openjdk:8
RUN mkdir /app
WORKDIR /app
EXPOSE 8080/TCP
CMD ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","-jar","springfox-plus-sample-1.0.1.jar"]
