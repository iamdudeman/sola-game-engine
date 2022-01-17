# Branch WIP

# TODO List
* Engine GUI Editor (Sola Editor)
  * Add more component controllers
    * Sprite
    * SpriteAnimator
  * editor camera improvements
    * add new entities to center of editor camera
    * better camera controls than WSAD
    * option to focus editor camera on selected entity?
    * option to copy camera's transform for editor preview of it?
  * Way to view all Systems and Components available
  * Way to view various EventListeners?
  * build for platform
    * JavaFx
    * Swing
    * Browser
  * Create child + parent relationships for the entities?
  * load additional components, systems and engine ui from external JAR
    * maybe generate a gradle file to build the jar from inside the engine?
* Rendering
  * SolaGraphics could cull entities that are outside the camera viewport
* Unit Testing
  * Add easy way to test the ability of ECS components to be serializable
  * Add lots of missing tests :)
* Research Virtual File System
* Browser Platform
  * Implement better tool for compiling Java to JavaScript code (TeaVM gradle plugin has some file locking issues)
  * Improve performance (StressTestExample can't handle a lot of objects)
  * Implement Font loading
  * Implement SpriteSheet loading
  * Implement window resizing using Viewport
* Tools
  * Export from Engine UI to installer of some sort
    * Include JRE and assets
* Audio
  * Interface for type
  * Implement loader per platform (Swing and JavaFX can probably share though)
* Build pipeline
  * Run full build for testing and code quality
  * Generate engine and platform artifacts
* Particle System
  * transform
  * speed
  * time left alive
  * color?
  * size?
* Gui Components
  * Panel
    * padding
    * parent Panel option for nesting panels
  * Text
    * should handle wrapping based on parent Panel?
  * Button
* Scripting language
  * Consider scrapping this features
  * How will that work with browser implementation?
  * Maybe a custom Domain Specific Language?
    * Perhaps JSON that describes what Systems to load with what settings
    * Describe Scenes and Entities / components
* Physics
  * Collision Detection
    * Collision layers / tags for ignoring specific collisions
    * Polygon x AABB
    * Polygon x Circle
    * Polygon x Polygon
* Camera
  * Possibly allow multiple cameras (think split screen games)
* Integrations
  * Steam?
    * include JRE in the output file
      * might vary per platform
* Android Platform
  * Implement
