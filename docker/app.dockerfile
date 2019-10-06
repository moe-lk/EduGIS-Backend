# FROM java:8
# WORKDIR /app
# ADD target/gis-*.jar /app/gis-api.jar
# ENTRYPOINT ["java","-jar","gis-api.jar"]
FROM maven:3.5-jdk-8
# COPY src /usr/src/app/src
# COPY pom.xml /usr/src/app
# RUN mvn -f /usr/src/app/pom.xml clean package
#
#
# VOLUME /tmp
# EXPOSE 8080
# ARG JAR_FILE
# # COPY target/dependencies/gis-*.jar gis-api.jar
# ENTRYPOINT ["java","-jar","/usr/src/app/target/gis-*.jar"]