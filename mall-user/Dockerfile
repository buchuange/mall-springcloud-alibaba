FROM java:8-jre
MAINTAINER zhongjunjie
ADD ./target/server-user.jar /opt
RUN chmod +x /opt/server-user.jar
CMD java -jar /opt/server-user.jar --spring.profiles.active=prod

EXPOSE 8000