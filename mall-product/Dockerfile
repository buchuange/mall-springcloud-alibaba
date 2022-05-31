FROM java:8-jre
MAINTAINER zhongjunjie
ADD ./target/server-product.jar /opt
RUN chmod +x /opt/server-product.jar
CMD java -jar /opt/server-product.jar --spring.profiles.active=prod

EXPOSE 8100