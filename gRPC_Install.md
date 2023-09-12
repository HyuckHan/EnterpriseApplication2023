### gRPC - Java 환경 구축

1. Protocol Buffer 설치

아래 웹 사이트에서 linux(x86-64) 버전을 다운로드. 
23년 9월 12일 기준 protoc-24.3-linux-x86_64.zip

https://github.com/google/protobuf/releases

```
$ cd ~/bigdata
$ mkdir protoc
$ cd protoc
$ wget https://github.com/protocolbuffers/protobuf/releases/download/v24.3/protoc-24.3-linux-x86_64.zip (위 웹 사이트에서 주소 복사)
$ unzip protoc-24.3-linux-x86_64.zip
$ sudo cp bin/* /usr/local/bin/
$ sudo cp -fr include/* /usr/local/include
```
