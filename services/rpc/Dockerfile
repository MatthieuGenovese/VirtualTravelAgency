FROM tomee:8-jre-7.0.3-plus
MAINTAINER Mohamed Chennouf (mohamed.chennouf@etu.unice.fr)

# Build with : docker build -t petitroll/tcs-rpc .
# Publish with: docker push petitroll/tcs-rpc

WORKDIR /usr/local/tomee/

COPY ./target/submittravel-service-rpc.war ./webapps/.

EXPOSE 8080