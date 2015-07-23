FROM java:8
VOLUME /tmp
EXPOSE 9000 8000 8081 8080
ADD product-service.jar app.jar
ADD classes/server.jks server.jks
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-Dserver.ssl.key-store=/server.jks","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
