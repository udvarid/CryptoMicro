docker kill crypto-jms
docker rm crypto-jms
docker run --name crypto-jms --rm -p 8161:8161 -p 61616:61616 vromero/activemq-artemis