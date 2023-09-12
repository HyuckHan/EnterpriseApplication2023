# HelloWorld (Gradle)

## Gradle Installation 

Visit https://services.gradle.org/distributions/.

최신 버전의 gradle binary 버전을 다운로드한다. 
2013-09-13일 기준으로 gradle-8.3-bin.zip

```
$ cd ~/bigdata
$ wget https://services.gradle.org/distributions/gradle-8.3-bin.zip
$ sudo mkdir /opt
$ sudo unzip -d /opt/gradle gradle-8.3-bin.zip
$ cd /opt
$ sudo ln -s gradle-8.3 gradle
```

~/.bashrc 파일에 아래 두 문장을 추가.
```
export GRADLE_HOME=/opt/gradle
export PATH=${GRADLE_HOME}/bin:${PATH}
```

## Generating Gradle Wrapper

```
$ gradle wrapper
```

## Build using gradlew

```
$ ./gradlew build
```
