FROM openjdk:17
COPY . ph-ee-connector-integration-test
RUN microdnf install findutils
RUN cd /ph-ee-connector-integration-test && ./gradlew compileJava --no-daemon
RUN cd /ph-ee-connector-integration-test && ./gradlew assemble --no-daemon || return 0
RUN cd /ph-ee-connector-integration-test && ./gradlew build -x test
WORKDIR /ph-ee-connector-integration-test
