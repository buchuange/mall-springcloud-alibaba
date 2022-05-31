FROM java:8-jre
MAINTAINER zhongjunjie
ADD ./target/server-gateway.jar /opt
RUN chmod +x /opt/server-gateway.jar
CMD java -jar /opt/server-gateway.jar --spring.profiles.active=prod

EXPOSE 9572