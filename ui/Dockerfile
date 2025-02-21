FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
        x11-apps \
            libxext6 \
            libxrender1 \
            libxtst6 \
            libxi6 \
            libx11-6 \
            libxau6 \
            libxdmcp6 \
            libfreetype6 \
            mesa-utils \
            && rm -rf /var/lib/apt/lists/*



COPY target/ui-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
