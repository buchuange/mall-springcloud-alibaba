FROM java:8-jre
MAINTAINER zhongjunjie
ADD ./target/server-order.jar /opt
RUN chmod +x /opt/server-order.jar
CMD java -jar /opt/server-order.jar --spring.profiles.active=prod

EXPOSE 8300