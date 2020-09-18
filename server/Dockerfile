FROM openjdk:8-jdk
RUN apt-get update && apt-get install openssh-server --assume-yes 
RUN sh -c '/bin/echo -e "y"' | ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key -N "" -q && \
sh -c '/bin/echo -e "y"' | ssh-keygen -t ecdsa -f /etc/ssh/ssh_host_ecdsa_key -N "" -q && \
sh -c '/bin/echo -e "y"' | ssh-keygen -t ed25519 -f /etc/ssh/ssh_host_ed25519_key -N "" -q
ARG APPLICATION_VERSION=0.0.1
ENV APPLICATION_VERSION ${APPLICATION_VERSION}
COPY build/libs/*.jar /app/
COPY application.yml /app/
COPY logback-spring.xml /app/logback-spring.xml
WORKDIR /app
CMD ["java", "-jar", "/app/suda-0.0.1-SNAPSHOT.jar"]
