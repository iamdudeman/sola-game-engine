## Known Bugs List

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

-----------------------------------------------------------------------------------------------------------------------

## TODO List
* SolaGui
  * Implement a way to load gui stuff from a file
  * possibly use JSON to define gui structure
  * A "post load" callback to add event listeners and such
* Figure out how to handle TouchInput
  * Primarily for browser but also could be supported in JavaFX
  * touchstart and touchend all hard coded to MouseButton.Primary currently
    * implement touchmove
  * Figure out TouchInput API
    * How to handle multitouch
  * Switch JsMouseUtils to mouse events instead of pointer events
* Rendering
  * Implement more BlendModes
  * Ability to change line width when drawing
* BrowserPlatform
  * modularize (requires figuring out how to modularize parts of teavm needed)
* Particle System
  * consider adding acceleration
  * consider some sort of "swaying" for non-linear particles
* Unit Testing
  * Add lots of missing tests :)
* JavaDocs
  * add missing JavaDocs
* Build files
  * See if `duplicatesStrategy = DuplicatesStrategy.EXCLUDE` can be removed
  * Make JavaFX and Swing examples into an executable jar of some sort
    * include assets with them
  * Add convenience gradle task for browser that zips web files and assets
* Consider adding a "debug console" option
  * While open can toggle things like render debug outlines and debug spacial hashmap stuff
  * Could also maybe allow adding custom commands
  * Should probably always use a "default font" if it is implemented
* Tooling
  * Research build tooling of some sort
    * Take a main java file and build for a platform maybe?
* Browser Platform
  * Consider web worker for game loop
    * main thread creates needed dom events
    * mouse and key events sent to worker
    * worker sends ImageData to main thread to render
    * (this approach may improve performance for StressTestExample to work better)
  * Improve performance (StressTestExample can't handle a lot of objects)
    * Possibly finish implementing BrowserCanvasRenderer?
* Research Virtual File System
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

-----------------------------------------------------------------------------------------------------------------------
