# URL Shortener

##Application Components:
    
1. Web Load Balancer (Ribon)
2. REST Service (Spring)
3. URL Encoder (Base64 like)
4. In memory data grid client (Redis)
5. Log replication client (Kafka)

Basically, given a long URL, the system generates a 32bits hash, which
is converted to integer, and then encoded in base 64 string.
The short and long url's are then saved in the in-memory data grid (Redis).
The last step is to send the pair (short url, long url) to the Kafka Cluster, 
which will replicate it to the other nodes.

This way the goal of Kafka is to guarantee availability, and Redis an efficient
and simple storage mechanism.

Having the load balance routing the requests to the web servers 
provides scalability to a certain stage.


##External Dependencies:
1. [https://kafka.apache.org/downloads|Kafka]
2. [http://download.redis.io/releases/redis-4.0.9.tar.gz|Redis]

##Load Balancer Configuration

1. file: urlshortenerproxy/src/main/resources/application.yml

At this file are defined the load balancer port and the nodes hostname + port.

## Kafka cluster configuration

 
####file : `config/server.properties`\
properties: 
* `broker.id=0`
* `listeners=PLAINTEXT://:9092`
* `log.dirs=/tmp/kafka-logs`
* `num.partitions=3`

** The other properties remain the same as original.

####file : `config/server-1.properties`\
properties: 
* `broker.id=1`
* `listeners=PLAINTEXT://:9093`
* `log.dirs=/tmp/kafka-logs-1`
* `num.partitions=3`

** The other properties remain the same as original.

####file : `config/server-2.properties`\
properties: 
* `broker.id=2`
* `listeners=PLAINTEXT://:9094`
* `log.dirs=/tmp/kafka-logs-2`
* `num.partitions=3`

** The other properties remain the same as original.

##Running KAFKA and REDIS
1. `cd ${KAFKA_HOME}`
2. `bin/zookeeper-server-start.sh config/zookeeper.properties`
3. `bin/kafka-server-start.sh config/server.properties`
4. `bin/kafka-server-start.sh config/server-1.properties`
5. `bin/kafka-server-start.sh config/server-2.properties`
6. `bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 3 --topic url-shortener`
7. `cd ${REDIS_HOME}/src`
8. `redis-server`

If we're running Kafka on different machines, then we need the same about Redis. If not, 
then a single instance should be fine.

##Modules:
1. urlshortenerproxy: load balancer, forwards requests to the rest servers
2. urlshortenerweb: rest services


##Spring Configuration files:
1. urlshortenerproxy/src/main/resources/application.yml + application.properties
2. urlshortenerweb/src/main/resources/application.properties


##Building

* `mvn package`\
The executable jar files will be located at the `target` folder inside 
each module.


##Running the rest servers
    
* `java -jar urlshortenerweb-0.1.0.jar --service.port=8081`
* `java -jar urlshortenerweb-0.1.0.jar --service.port=8082`
* `java -jar urlshortenerweb-0.1.0.jar --service.port=8083`

##Running the Web Load Balancer
    
`java -jar ./url-shortener-proxy-0.1.0.jar --service.port=8080`

##Test:
    
* `curl -G http://localhost:8080/create?url=XXX`
* `curl -G http://localhost:8080/lookup?url=XXX`
* Alternatively you can also use the browser.
