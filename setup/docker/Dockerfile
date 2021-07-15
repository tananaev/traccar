# Pull Maven with JDK 7 for build time docker
FROM maven:3.5.4-jdk-7-slim AS build

MAINTAINER Dan Peleg <dan@globekeeper.com>

COPY . .
RUN mkdir -p tmp
RUN mvn package
RUN cat setup/traccar.xml | awk '/config.default/ && !modif { print;printf("    <entry key=\"web.debug\">true</entry>\n");next; modif=1 } {print}'  > tmp/traccar.xml
RUN cp -rf /setup/default.xml tmp \
		&& cp -rf /schema tmp/schema \
		&& cp -rf /templates tmp/templates \
		&& cp -rf /target/tracker-server.jar tmp/traccar-server.jar \
		&& cp -rf /target/lib tmp/lib \
		&& if [ -d /traccar-web/web ] ; \
			then cp -rf /traccar-web/web tmp/web ; \
			else mkdir tmp/web ; fi

# Pull java 8 runtime for running traccar
FROM java:8-alpine
RUN apk upgrade --update && \
    apk add --update curl bash && \
    rm -rf /var/cache/apk/* && \
    mkdir -p /opt/traccar/logs && \
    mkdir -p /opt/traccar/data

ENV JAVA_OPTS -Xms256m -Xmx1024m

# only copy needed binaries 
COPY --from=build ./tmp/traccar.xml /opt/traccar/traccar.xml
COPY --from=build ./tmp/default.xml /opt/traccar/conf/default.xml
COPY --from=build ./tmp/schema /opt/traccar/schema
COPY --from=build ./tmp/templates /opt/traccar/templates
COPY --from=build ./tmp/web /opt/traccar/web
COPY --from=build ./tmp/lib /opt/traccar/lib
COPY --from=build ./tmp/traccar-server.jar /opt/traccar/traccar-server.jar

EXPOSE 8082
EXPOSE 5000-5150

WORKDIR /opt/traccar

ENTRYPOINT ["java","-jar","traccar-server.jar","traccar.xml"]