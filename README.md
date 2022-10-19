# DataLockLib
___

## **Developer API**
Repository
```kotlin
repositories {
    maven {
        url = uri("https://repo.destro.xyz/snapshots")
    }
}

dependencies {
    compileOnly("com.alttd.datalock:api:1.0.0-SNAPSHOT")
}
```

## **Usage**
An instance of DataLock can be obtained by using this code. 
```
DataLockAPI datalock = DataLockAPI.get();
```

## **Downloads**
There are currently no downloads provided. Jars can only be obtained by building from source.

## **Building**

#### Initial setup
Clone the repo using `git clone https://github.com/Altitude-Devs/DataLockLib.git`.

#### Building
Use the command `./gradlew build --stacktrace` in the project root directory.
The compiled jar will be placed in directory `/plugin/build/libs/`.

## **Commands**

| Command             | Description                 | Permission                  |
|---------------------|-----------------------------|-----------------------------|

## **Permissions**

| Permission                  | Description                                    |
|-----------------------------|------------------------------------------------|

## **Configuration**

```yaml

```

## **Support**

## **License**
[LICENSE](LICENSE)