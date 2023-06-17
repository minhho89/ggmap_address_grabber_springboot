FROM bellsoft/liberica-openjdk-alpine-musl:17
COPY target/GGMapAddGrabber.jar GGMapAddGrabber.jar
ENTRYPOINT ["java","-jar","/GGMapAddGrabber.jar"]