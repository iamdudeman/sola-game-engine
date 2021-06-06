# TODO List
* Browser Platform
  * Improve performance (StressTestExample can't handle a lot of objects)
* Android Platform
  * Implement
* Input
  * Mouse input handling
* Camera
  * Figure out how to implement
    * Translate
    * Scale / zoom
* Renderer
  * DrawString method(s)
    * DrawString could use this bitmap to actually draw the pixels with the desired color
* Tools
  * Tool to rasterize fonts into a bitmap + font information files
  * Tool to export browser platform Main java method instead of using gradle plugin
* Animation
  * Component and System
* Scene
  * Implement a good abstraction for this (maybe use the one SolKana had)
* Audio
  * Interface for type
  * Implement loader per platform (Swing and JavaFX can probably share though)
* GUI
  * Elements
    * Text
    * Button
  * Layout
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
* Matrix2D
  * Implement inverse
  * Implement reflection
  * Implement orthogonal projection
