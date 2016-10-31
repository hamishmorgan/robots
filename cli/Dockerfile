# DOCKER-VERSION 1.12

FROM openjdk:8-jre

MAINTAINER Hamish Morgan <hamish.morgan@gmail.com>

ENV ARCHIVE robots-cli-bin-with-deps.tar.gz

ADD target/$ARCHIVE /

WORKDIR /robots-cli

ENTRYPOINT ["/robots-cli/robots"]
