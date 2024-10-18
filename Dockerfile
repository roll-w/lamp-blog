FROM eclipse-temurin:21-jre-alpine

LABEL org.opencontainers.image.authors="rollw"
LABEL org.opencontainers.image.title="Lamp Blog"

ARG LAMP_VERSION="0.1.0"

ADD ./build/dist/lamp-blog-${LAMP_VERSION}-dist.tar.gz /app
RUN mv /app/lamp-blog-${LAMP_VERSION} /app/lamp-blog
WORKDIR /app/lamp-blog

EXPOSE 5100:5100

ENV JAVA_OPTS=""

# TODO: support args, envs
ENTRYPOINT ["bin/lamp"]
