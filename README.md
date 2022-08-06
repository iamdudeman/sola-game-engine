# Branch WIP



# TODO List
* Rendering
  * SolaGraphics could cull entities that are outside the camera viewport
  * More BlendMode implementations
* SolaGui Stuff
  * GuiElementContainer
    * Consider adding anchor support to Stream or a new container to be able to easily center things?
* Research build tooling of some sort
  * Take a main java file and build for a platform maybe?
  * would be later used in Engine GUI Editor
* Unit Testing
  * Add easy way to test the ability of ECS components to be serializable
  * Add lots of missing tests :)
* Browser Platform
  * Consider web worker for game loop
    * main thread creates needed dom events
    * mouse and key events sent to worker
    * worker sends ImageData to main thread to render
    * (this approach may improve performance for StressTestExample to work better)
  * Improve performance (StressTestExample can't handle a lot of objects)
  * Implement Audio loading
  * Implement window resizing using Viewport
    * Maybe add extra DOM elements to allow resizing?
* Research Virtual File System
* Build pipeline
  * Generate engine and platform artifacts
* Engine GUI Editor (Sola Editor)
  * GuiElement stuff
  * Add more component controllers
    * SpriteAnimator
  * editor camera improvements
    * add new entities to center of editor camera
    * better camera controls than WSAD
    * option to focus editor camera on selected entity?
    * option to copy camera's transform for editor preview of it?
  * Way to view all Systems and Components available
  * Way to view various EventListeners?
  * Allow Material assets to be updated
  * Add sprite sheets by image ui
    * Ability to update them later to add new sprite ids
  * maybe show collider outline when collider component is active in editor?
    * if not this a toggle to view all collider outlines
  * build for platform
    * JavaFx
      * maybe include JRE
    * Swing
      * maybe include JRE
    * Browser
  * Create child + parent relationships for the entities?
  * load additional components, systems and engine ui from external JAR
    * maybe generate a gradle file to build the jar from inside the engine?
* Scripting language
  * How will that work with browser implementation?
  * Maybe a custom Domain Specific Language?
    * Perhaps JSON that describes what Systems to load with what settings
    * Describe Scenes and Entities / components
  * Needs to be able to update ECS things
  * Needs to be able to assign mouse hover and click callbacks
  * Needs to be able to assign keyboard press callbacks
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
