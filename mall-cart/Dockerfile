FROM java:8-jre
MAINTAINER zhongjunjie
ADD ./target/server-cart.jar /opt
RUN chmod +x /opt/server-cart.jar
CMD java -jar /opt/server-cart.jar --spring.profiles.active=prod

EXPOSE 8200