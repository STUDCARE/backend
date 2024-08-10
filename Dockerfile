FROM amazoncorretto:17.0.4
WORKDIR /app
ADD build/libs/stud-care-backend-service-latest.jar stud-care-backend-service.jar
RUN chown 10001:10001 /app
USER 10001
EXPOSE 9090
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT java ${JAVA_OPTS} ${OTEL_ARGS} -jar /app/stud-care-backend-service.jar