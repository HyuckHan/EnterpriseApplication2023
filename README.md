# EnterpriseApplication2023

## Prerequisite

### Java11, Ant 설치

```
$ sudo apt-get update
$ sudo apt-get install openjdk-11-jdk ant
```

아래의 커맨드를 이용해서 ~/.bashrc 수정
```
$ vi ~/.bashrc
```

~/.bashrc에 아래의 내용을 추가하거나 java-8의 내용을 있으면 아래의 내용으로 변경

```
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
```

### Gradle 설치

Visit https://services.gradle.org/distributions/.

최신 버전의 gradle binary 버전을 다운로드한다.
2023-09-13일 기준으로 gradle-8.3-bin.zip

```
$ cd /tmp
$ wget https://services.gradle.org/distributions/gradle-8.3-bin.zip
$ sudo mkdir /opt
$ sudo unzip -d /opt gradle-8.3-bin.zip
$ cd /opt
$ sudo ln -s gradle-8.3 gradle
```

~/.bashrc 파일에 아래 두 문장을 추가.
```
export GRADLE_HOME=/opt/gradle
export PATH=${GRADLE_HOME}/bin:${PATH}
```
