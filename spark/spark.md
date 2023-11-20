# Spark 설치(Oracle VM)

```
$ cd ~
$ tar -xvf bigdata/spark-3.2.1-bin-hadoop3.2.tgz
$ ln -s spark-3.2.1-bin-hadoop3.2 spark
```

~/.bashrc의 마지막에 아래의 두 환경 변수 설정
```
export SPARK_HOME=~/spark
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
```
