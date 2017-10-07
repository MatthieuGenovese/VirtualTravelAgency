FROM tomee:8-jre-7.0.3-plus
MAINTAINER Génovèse Matthieu

# Build with : docker build -t travelagency/flightreservation-rest .
# Publish with: docker push travelagency/flightreservation-rest

WORKDIR /usr/local/tomee/

COPY ./target/flightreservation-service-document.war ./webapps/.

EXPOSE 8080
