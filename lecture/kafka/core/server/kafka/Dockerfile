FROM --platform=linux/amd64 ubuntu:latest

ARG BROKER_ID
ARG BOOTSTRAP_SERVER
ARG PORT

RUN apt-get install
RUN apt-get update
RUN apt-get install wget -y sudo
RUN apt-get install -y openjdk-17-jdk

RUN useradd -ms /bin/bash kafka
USER kafka
WORKDIR /home/kafka

RUN wget https://archive.apache.org/dist/kafka/3.1.2/kafka_2.13-3.1.2.tgz
RUN tar xvf kafka_2.13-3.1.2.tgz
RUN ln -s kafka_2.13-3.1.2 kafka

RUN rm ./kafka/config/server.properties
COPY --chown=kafka:kafka ./config/server.properties ./kafka/config/server.properites

RUN sed -i "/broker.id=0/c\broker.id=${BROKER_ID}" ./kafka/config/server.properites
RUN sed -i "/advertised.listeners=PLAINTEXT:\/\/kafka:9092/c\advertised.listeners=PLAINTEXT:\/\/${BOOTSTRAP_SERVER}:${PORT}" ./kafka/config/server.properites
RUN sed -i "/#listeners=PLAINTEXT:\/\/:9092/c\listeners=PLAINTEXT:\/\/:${PORT}" ./kafka/config/server.properites

ENV KAFKA_HEAP_OPTS="-Xmx400m -Xms400m"

CMD [ "./kafka/bin/kafka-server-start.sh", "./kafka/config/server.properites" ]

#bin/kafka-topics.sh --create --bootstrap-server kafka-1:9092 --replication-factor 2 --partitions 3 --topic select-color
#bin/kafka-topics.sh --list --bootstrap-server kafka-1:9092