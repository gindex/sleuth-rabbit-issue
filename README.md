### Sample to reproduce the [issue](https://github.com/spring-cloud/spring-cloud-sleuth/issues/1715#issuecomment-680977833)

Steps to reproduce

1. `docker-compose up -d`
2. `./gradlew build`
3. `java -jar  build/libs/sleuth-rabbit-0.0.1-SNAPSHOT.jar`


Expected that all send messages have sample state `0`, because of  `SamplerFunction<MessagingRequest>` returning false in all cases.
However, the first ten messages have the state `1`. 