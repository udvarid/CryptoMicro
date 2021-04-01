#!/bin/bash
echo clean up docker images
docker system prune -af

#echo Backend application is building
#cd cryptoCollector/
#mvn clean install -DskipTests
#
#echo building docker image
#docker build . -t udvaridonat/donat-crypto:cryptoCollector
#
#echo pushing up the image to my repository
#docker push udvaridonat/donat-crypto:cryptoCollector
#
#cd ..
#
#cd cryptoReporter/
#mvn clean install -DskipTests
#
#echo building docker image
#docker build . -t udvaridonat/donat-crypto:cryptoReporter
#
#echo pushing up the image to my repository
#docker push udvaridonat/donat-crypto:cryptoReporter
#
#cd ..
#
#cd cryptoUser/
#mvn clean install -DskipTests
#
#echo building docker image
#docker build . -t udvaridonat/donat-crypto:cryptoUser
#
#echo pushing up the image to my repository
#docker push udvaridonat/donat-crypto:cryptoUser
#
#cd ..

cd cryptoFrontend/crypto/

echo refresh angular dependencies
npm i

echo building project
ng build --prod

echo build the image
docker build . -t udvaridonat/donat-crypto:frontend

echo push image to my repository
docker push udvaridonat/donat-crypto:frontend

cd ../..

echo clean up docker images
docker system prune -af

