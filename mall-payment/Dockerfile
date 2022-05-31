FROM java:8-jre
MAINTAINER zhongjunjie
ADD ./target/server-payment.jar /opt
RUN chmod +x /opt/server-payment.jar
CMD java -jar /opt/server-payment.jar --spring.profiles.active=prod

EXPOSE 8400