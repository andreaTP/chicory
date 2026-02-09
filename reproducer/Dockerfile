FROM ubuntu:24.04

ARG JDK_REPO=https://github.com/openjdk/jdk.git
ARG JDK_COMMIT=jdk-17-ga
ARG PATCH=

# Label with PATCH value to ensure cache invalidation when PATCH changes
LABEL patch="${PATCH}"

RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y \
      git build-essential autoconf make zip unzip \
      ca-certificates pkg-config file \
      openjdk-17-jdk \
      libx11-dev libxext-dev libxrender-dev libxtst-dev libxt-dev \
      libasound2-dev libcups2-dev libfontconfig1-dev libfreetype6-dev \
      libxrandr-dev libxinerama-dev libffi-dev \
      libc6-dev && \
    rm -rf /var/lib/apt/lists/*

ENV BOOT_JDK=/usr/lib/jvm/java-17-openjdk-amd64

# Use PATCH in RUN to ensure cache invalidation
RUN echo "Building with PATCH=${PATCH}" && \
    git clone "${JDK_REPO}" /opt/jdk && \
    cd /opt/jdk && \
    git config user.email "build@docker" && \
    git config user.name "Docker Build" && \
    git checkout "${JDK_COMMIT}" && \
    if [ -n "${PATCH}" ]; then \
      git cherry-pick "${PATCH}"; \
    fi

RUN cd /opt/jdk && \
    bash configure \
      --with-boot-jdk="${BOOT_JDK}" \
      --with-debug-level=fastdebug \
      --with-jvm-variants=server \
      --disable-warnings-as-errors && \
    make images

ENV JDK_HOME=/opt/jdk/build/linux-x86_64-server-fastdebug/jdk
ENV PATH="${JDK_HOME}/bin:${PATH}"

WORKDIR /work

COPY . .

CMD ["./run.sh"]
