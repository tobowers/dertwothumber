FROM java:8

RUN cd /usr/src \
    && wget -O /usr/src/dynamodb_local.tar.gz http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.tar.gz \
    && tar -xzf dynamodb_local.tar.gz

WORKDIR /usr/src

EXPOSE 8000

CMD ["java", "-Djava.library.path=./DynamoDBLocal_lib", "-jar", "DynamoDBLocal.jar"]
