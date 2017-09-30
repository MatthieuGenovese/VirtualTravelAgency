FROM tomee:8-jre-7.0.3-plus
MAINTAINER Génovèse Matthieu

# Build with : docker build -t travelagency/flyreservation-rest .
# Publish with: docker push travelagency/flyreservation-rest

WORKDIR /usr/local/tomee/

COPY ./target/carshotels-service-document.war ./webapps/.

EXPOSE 8080
