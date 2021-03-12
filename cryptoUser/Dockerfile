FROM openjdk:8
RUN mkdir -p /usr/src/myapp
COPY ./target/cryptoUser-0.0.1-SNAPSHOT.war /usr/src/myapp
WORKDIR /usr/src/myapp
ENV CRYPTO_WAR="/usr/src/myapp/cryptoUser-0.0.1-SNAPSHOT.war"
CMD ["sh", "-c", "java -jar $CRYPTO_WAR"]