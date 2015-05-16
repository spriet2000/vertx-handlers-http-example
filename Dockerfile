FROM vertx/vertx3

EXPOSE 8080

RUN wget http://wwwftp.ciril.fr/pub/apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz
RUN tar -zxvf apache-maven-3.0.5-bin.tar.gz -C /usr/local && rm apache-maven-3.0.5-bin.tar.gz

ADD pom.xml data/pom.xml
ADD src data/src
ADD app data/app
RUN /usr/local/apache-maven-3.0.5/bin/mvn install

