# sola game engine
sola game engine is a zero external dependency game engine (other than teavm for transpiling to JavaScript) that has been a hobby project for a long time. Two larger pieces of this project were broken into separate repositories for easier maintenance (the entity component system and JSON parser). Its purpose has been primarily for learning about how games are developed from the ground up, but I hope to one day use it to also make some small games

[![Java CI](https://github.com/iamdudeman/sola-game-engine/actions/workflows/ci_build.yml/badge.svg)](https://github.com/iamdudeman/sola-game-engine/actions/workflows/ci_build.yml)
[![Javadocs Link](https://img.shields.io/badge/Javadocs-blue.svg)](https://iamdudeman.github.io/sola-game-engine/)
[![](https://jitpack.io/v/iamdudeman/sola-game-engine.svg)](https://jitpack.io/#iamdudeman/sola-game-engine)

## Download
### Gradle + Jitpack:
```kotlin
repositories {
  maven {
    url = uri("https://jitpack.io")
  }
}

dependencies {
  implementation("com.github.iamdudeman.sola-game-engine:sola-engine:<VERSION>")
  implementation("com.github.iamdudeman.sola-game-engine:platform-swing:<VERSION>")
  implementation("com.github.iamdudeman.sola-game-engine:tooling:<VERSION>")
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

## Quick Start
The general structure of a Sola project is to create an instance of [Sola](sola-engine/src/main/java/technology/sola/engine/core/Sola.java)
and then choose an implementation of [SolaPlatform](sola-engine/src/main/java/technology/sola/engine/core/SolaPlatform.java)
to "play" the Sola.

```java
public class Main {
    public static void main(String[] args) {
      SolaPlatform solaPlatform = new SwingSolaPlatform();
      Sola sola = new ExampleSola();

      solaPlatform.play(sola);
    }
}
```

You can use the [Sola Game Template repo](https://github.com/iamdudeman/sola-game-template) as a template to start
your repo using Sola Game Engine.

## Example Code
* [Sola examples](examples/common)
* [Swing Platform example](examples/swing)
* [JavaFX Platform example](examples/javafx)
* [Browser Platform example](examples/browser)
  * For browser the [GenerateBrowserFilesMain](examples/browser/src/main/java/technology/sola/engine/examples/browser/GenerateBrowserFilesMain.java) is responsible for generating the JavaScript files
  * The [BrowserMain](examples/browser/src/main/java/technology/sola/engine/examples/browser/BrowserMain.java) is the entry point GenerateBrowserFilesMain uses.
  * Then [DevServerMain](examples/browser/src/main/java/technology/sola/engine/examples/browser/DevServerMain.java) acts as a simple http server for the files

## Games Currently Using sola-game-engine
* [Acid Rain](https://iamdudeman.itch.io/acid-rain)
