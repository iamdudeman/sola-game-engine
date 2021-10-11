# Branch WIP

# TODO List
* Camera
  * Figure out how to implement
    * Translate
    * Scale / zoom
  * Possibly multiple cameras?
    * Camera output to section of a screen (allows split screen then)?
  * Screen units vs world units?
* Once Camera is figured out need a way to translate screen coordinates to world coordinates easily
* GUI
  * Elements
    * Text
    * Button
  * Layout
* Scene
  * Implement a good abstraction for this (maybe use the one SolKana had)
  * Save / load methods would be good (or a serializer / deserializer)
* Research Virtual File System
* Browser Platform
  * Improve performance (StressTestExample can't handle a lot of objects)
  * Implement Font loading
  * Implement SpriteSheet loading
  * Implement window resizing using Viewport
* Android Platform
  * Implement
* Tools
  * Export from Engine UI to installer of some sort
    * Include JRE and assets
  * Tool to export browser platform Main java method instead of using gradle plugin
* Audio
  * Interface for type
  * Implement loader per platform (Swing and JavaFX can probably share though)
* Build pipeline
  * Run full build for testing and code quality
  * Generate engine and platform artifacts
* Scripting language
  * How will that work with browser implementation?
  * Maybe a custom Domain Specific Language?
    * Perhaps JSON that describes what Systems to load with what settings
    * Describe Scenes and Entities / components
* Physics
  * Rotation
  * Collision Detection
    * Collision layers / tags for ignoring specific collisions
    * Polygon x AABB
    * Polygon x Circle
    * Polygon x Polygon
* Particle System
  * position
  * velocity
  * time left alive
  * color?
  * size?
* Engine GUI Editor (Sola Editor)
  * Way to view all Systems and Components available
  * Way to view various EventListeners?
  * Entity editor?
    * primitive types could generate a dialog of some sort
    * saves to json file
  * could load in multiple files
    * edit configurations go to appropriate files
  * play mode for quick testing
  * export stuff somehow maybe?
  * GUI asset manager
    * Allows adding images and via file selector
    * Game has a project folder structure to add/remove files
* Integrations
  * Steam?
    * include JRE in the output file
      * might vary per platform
