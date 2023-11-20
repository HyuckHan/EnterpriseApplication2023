# WordCount (Gradle)

## Generate Gradle Wrapper

```
$ gradle wrapper
```

## Build using gradlew

```
$ ./gradlew build
```

## Run 

```
$ spark-submit --class JavaWordCount  wordcount_gradle.jar inputfile outputfolder
```
