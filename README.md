# sola game engine

sola game engine is a zero external dependency game engine (other than teavm for transpiling to JavaScript) that has
been a hobby project for a long time. Two larger pieces of this project were broken into separate repositories for
easier maintenance (the entity component system and JSON parser). Its purpose has been primarily for learning about how
games are developed from the ground up, but I hope to one day use it to also make some small games.

You can view a collection of demos built with the sola game engine running in your browser at this
[itch.io page](https://iamdudeman.itch.io/sola-game-engine-demos).

[![Java CI](https://github.com/iamdudeman/sola-game-engine/actions/workflows/ci_build.yml/badge.svg)](https://github.com/iamdudeman/sola-game-engine/actions/workflows/ci_build.yml)
[![Javadocs Link](https://img.shields.io/badge/Javadocs-blue.svg)](https://iamdudeman.github.io/sola-game-engine/)
[![](https://jitpack.io/v/iamdudeman/sola-game-engine.svg)](https://jitpack.io/#iamdudeman/sola-game-engine)

## Quick Start

You can use the [Sola Game Template repo](https://github.com/iamdudeman/sola-game-template) as a template to start
your repo using the sola game engine.

The general structure of a Sola project is to first create an instance
of [Sola](sola-engine/src/main/java/technology/sola/engine/core/Sola.java). This is where all your amazing game code
goes! Then you choose an implementation
of [SolaPlatform](sola-engine/src/main/java/technology/sola/engine/core/SolaPlatform.java)
that has all the bindings for your desired platform to actually "play" the Sola.

```java
public class Main {
    public static void main(String[] args) {
        SolaPlatform solaPlatform = new SwingSolaPlatform();
        Sola sola = new ExampleSola();

        solaPlatform.play(sola);
    }
}
```

## Download

### Gradle + Jitpack:

```kotlin
repositories {
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.github.iamdudeman.sola-game-engine:sola-engine:SOLA_ENGINE_VERSION")
    implementation("com.github.iamdudeman.sola-game-engine:platform-swing:SOLA_ENGINE_VERSION")
    implementation("com.github.iamdudeman.sola-game-engine:tooling:SOLA_ENGINE_VERSION")
}
```

## Internally developed libraries uses

* [sola-json](https://github.com/iamdudeman/sola-json)
* [sola-ecs](https://github.com/iamdudeman/sola-ecs)

## Supported Platforms

* [Swing](sola-engine/platform/swing)
* [JavaFX](sola-engine/platform/javafx)
* [Browser](sola-engine/platform/browser)
    * Utilizes [teavm](https://github.com/konsoletyper/teavm) for transpiling to JavaScript

## Example Code

You can execute the example code in the browser at https://iamdudeman.itch.io/sola-game-engine-demos. The local server
will have to be downloaded and executed separately.

* [Sola examples](examples/common)
* [Swing Platform example](examples/swing)
* [JavaFX Platform example](examples/javafx)
* [Browser Platform example](examples/browser)
    * For browser
      the [GenerateBrowserFilesMain](examples/browser/src/main/java/technology/sola/engine/examples/browser/GenerateBrowserFilesMain.java)
      is responsible for generating the JavaScript files
    * The [BrowserMain](examples/browser/src/main/java/technology/sola/engine/examples/browser/BrowserMain.java) is the
      entry point GenerateBrowserFilesMain uses.
    * Then
      [DevBrowserFileServerMain](examples/browser/src/main/java/technology/sola/engine/examples/browser/DevBrowserFileServerMain.java)
      acts as a simple http server for the files
* [Server example](examples/server)

## Games using sola game engine

1. sola game engine demos
    * Play it on its [itch.io page](https://iamdudeman.itch.io/sola-game-engine-demos)
2. Acid Rain [OLC CodeJam 2022](https://itch.io/jam/olc-codejam-2022/entries)
    * Play it on its [itch.io page](https://iamdudeman.itch.io/acid-rain)
    * Check out its [GitHub repo](https://github.com/iamdudeman/acid-rain)
3. re;memory [OLC CodeJam 2023](https://itch.io/jam/olc-codejam-2023/entries)
    * Play it on its [itch.io page](https://iamdudeman.itch.io/rememory)
    * Check out its [GitHub repo](https://github.com/iamdudeman/re-memory)

## Packaging for release

### Browser zip file

Run the following gradle command

```shell
.\gradlew.bat distWebZip
```

The output will be at `examples/browser/build/examples-browser-<version>.zip`.
This can be deployed to places like `itch.io` when using the "HTML" project type.

### Swing + JavaFx fat jar

Run the following gradle command

```shell
.\gradlew.bat distFatJar
```

The output will be at `examples/swing/build/swing-<version>.jar` and `examples/javafx/build/javafx-<os>-<version>.jar`.
Your users will need to have Java 17 installed to run the jar.

### Swing + JavaFx .exe

You also have the option to use [jpackage](
https://docs.oracle.com/en/java/javase/17/jpackage/packaging-overview.html) to create an executable exe file.
Your users will not need to have Java installed.

1. Install [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Update $JAVA_HOME path environment variable
    * ex. C:\Program Files\Java\jdk-17.0.5
    * To test configuration run: `jpackage --version`
        * Should see the current jdk version returned: `17.0.5`
3. Run the following gradle command

```shell
.\gradlew.bat distFatJarZip
```

4. Output will be in the `build/jpackage` directory


## JSON Schema

[JSON schema files](json-schema) are provided for various asset types. These can assist you in creating valid assets for
the sola game engine to load when manually creating or updating them.

### IntelliJ setup

1. Open settings
2. Go to `Languages & Frameworks | Schemas and DTDs | JSON Schema Mappings`
3. Click `+` and select the schema file to add
4. Add by file path pattern (recommendations below)
    * SpriteSheet.schema.json -> *.sprites.json
    * Font.schema.json -> *.font.json
    * GuiDocument.schema.json -> *.gui.json
