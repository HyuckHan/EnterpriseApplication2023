# Kafka

## Zookeepr 설치

최신 Kafka를 다운로드. 아래 웹 사이트 방문. (Scala 2.12)
```
https://kafka.apache.org/downloads
```

2023-10-15일 기준 3.6.0 (scala 2.12)
```
$ cd /tmp 
$ wget https://downloads.apache.org/kafka/3.6.0/kafka_2.12-3.6.0.tgz
$ sudo tar -xvf /tmp/kafka_2.12-3.6.0.tgz -C /usr/local/
$ sudo ln -s /usr/local/kafka_2.12-3.6.0 /usr/local/kafka
```

Kafka server 시작
```
$ sudo /usr/local/kafka/bin/kafka-server-start.sh -daemon /usr/local/kafka/config/server.properties
```

Kafka topic list
```
$ /usr/local/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

Kafka topic 생성
```
$ /usr/local/kafka/bin/kafka-topics.sh --create --topic test_topic_00 --bootstrap-server localhost:9092
$ /usr/local/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
$ /usr/local/kafka/bin/kafka-topics.sh --describe --topic test_topic_00 --bootstrap-server localhost:9092
```

Sample Producer
```
/usr/local/kafka/bin/kafka-console-producer.sh --topic test_topic_00 --bootstrap-server localhost:9092
```

Sample Consumer
```
/usr/local/kafka/bin/kafka-console-consumer.sh --topic test_topic_00 --bootstrap-server localhost:9092
```
