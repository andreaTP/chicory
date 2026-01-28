FROM eclipse-temurin:17
# FROM eclipse-temurin:25

RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /workspace

COPY . /workspace/

RUN mvn -Dquickly

CMD ["./reproducer.sh"]
