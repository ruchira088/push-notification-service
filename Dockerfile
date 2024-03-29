FROM openjdk:8-jdk

RUN apt-get update && \
    apt-get install apt-transport-https ca-certificates software-properties-common -y && \
    echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 && \
    apt-get update && \
    apt-get install sbt -y

WORKDIR /opt/push-notification-service

COPY . .

EXPOSE 9000 5005

ENTRYPOINT ["sbt", "-jvm-debug", "5005"]

CMD ["~run"]