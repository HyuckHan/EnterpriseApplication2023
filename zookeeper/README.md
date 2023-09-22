# Zookeeper

## Zookeepr 설치

최신 release를 다운로드. 아래 웹 사이트 방문.
```
https://zookeeper.apache.org/releases.html#download
```

2023-09-21일 기준 3.9.0
```
$ cd /tmp 
$ wget https://dlcdn.apache.org/zookeeper/zookeeper-3.9.0/apache-zookeeper-3.9.0-bin.tar.gz
$ sudo tar -xvf /tmp/apache-zookeeper-3.9.0-bin.tar.gz -C /usr/local
$ sudo ln -s /usr/local/apache-zookeeper-3.9.0-bin /usr/local/apache-zookeeper
```

configuration 파일을 생성하고 dataDir 값 변경
```
$ cd /usr/local/apache-zookeeper 
$ sudo mkdir data
$ cd conf
$ sudo cp zoo_sample.cfg zoo.cfg
$ sudo vi zoo.cfg
```
zoo.cfg에서 아래 설정을 변경

dataDir=/tmp/zookeeper
--> dataDir=/usr/local/apache-zookeeper/data

zookeeper 시작
```
$ cd /usr/local/apache-zookeeper 
$ sudo bin/zkServer.sh start
```
