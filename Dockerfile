FROM eclipse-temurin:21-jre-alpine

LABEL org.opencontainers.image.authors="rollw"
LABEL org.opencontainers.image.title="Lampray"

ARG LAMPRAY_VERSION="0.1.0"

ADD ./build/dist/lampray-${LAMPRAY_VERSION}-dist.tar.gz /app
RUN mv /app/lampray-${LAMPRAY_VERSION} /app/lampray
WORKDIR /app/lampray

EXPOSE 5100

ENV JAVA_OPTS=""

# TODO: support args, envs
ENTRYPOINT ["bin/lampray"]
