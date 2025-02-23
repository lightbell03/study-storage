FROM --platform=arm64 ubuntu:latest

RUN apt-get install -y
RUN apt-get update -y
RUN apt-get install wget -y
RUN apt-get install openjdk-17-jdk -y

RUN useradd -ms /bin/bash zookeeper
USER zookeeper
WORKDIR /home/zookeeper

RUN wget https://archive.apache.org/dist/kafka/3.1.2/kafka_2.12-3.1.2.tgz
RUN tar xvf kafka_2.12-3.1.2.tgz
RUN ln -s kafka_2.12-3.1.2 kafka

EXPOSE 2181

CMD [ "./kafka/bin/zookeeper-server-start.sh", "./kafka/config/zookeeper.properties"]