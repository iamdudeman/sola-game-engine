# sola-game-engine TODO list

* maybe gui themes
    * maybe default light and dark theme
    * sets defaults for all gui elements
        * focus, borders, backgrounds, text, etc.

## Known Bugs List

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

-----------------------------------------------------------------------------------------------------------------------

## TODO List

* gui element hidden enhancement of some sort
    * no way to "display none" an element (can currently only hide while taking up space still)
* networking
  * rest client interface
    * javafx/swing impl
    * browser impl
  * simple rest server
* Rendering
  * Ability to change line width when drawing
  * Implement more BlendModes
  * Add a way to do gradients
    * maybe just linear at first
* BrowserPlatform
  * modularize (requires figuring out how to modularize parts of teavm needed)
* Particle System
  * consider adding acceleration
  * consider some sort of "swaying" for non-linear particles
* Unit Testing
  * Add lots of missing tests :)
* JavaDocs
  * add missing JavaDocs
* Consider mechanism to "batch" together SolaGraphics entity draws when they are in the same layer
  * Currently, it does each one individually
* SolaGui
  * Implement a way to load gui stuff from a file
    * possibly use JSON to define gui structure
  * A "post load" callback to add event listeners and such
* Consider some sort of lighting mechanism implementation
* Consider adding rotation to TransformComponent
  * Would need to update rendering stuff
  * Would need to update physics stuff
* Physics
  * Collision Detection
    * Polygon x AABB
    * Polygon x Circle
    * Polygon x Polygon
* Figure out how to handle TouchInput
  * Primarily for browser but also could be supported in JavaFX
  * touchstart and touchend all hard coded to MouseButton.Primary currently
    * implement touchmove
  * Figure out TouchInput API
    * How to handle multitouch
  * Switch JsMouseUtils to mouse events instead of pointer events
* Consider adding a "debug console" option
  * While open can toggle things like render debug outlines and debug spacial hashmap stuff
  * Could also maybe allow adding custom commands
  * Should probably always use a "default font" if it is implemented
* Browser Platform
  * Consider web worker for game loop
    * main thread creates needed dom events
    * mouse and key events sent to worker
    * worker sends ImageData to main thread to render
    * (this approach may improve performance for StressTestExample to work better)
  * Improve performance (StressTestExample can't handle a lot of objects)
    * Possibly finish implementing BrowserCanvasRenderer?
* Research Virtual File System
  * ability to mount archives of some sort
    * possible example, instead of png use different file format that many can be compressed into one larger file
      * path /test
      * contents 100 100 ffffffff 00ff00ff
* Scripting language
  * How will that work with browser implementation?
  * Maybe a custom Domain Specific Language?
    * Perhaps JSON that describes what Systems to load with what settings
    * Describe Scenes and Entities / components
  * Needs to be able to update ECS things
  * Needs to be able to assign mouse hover and click callbacks
  * Needs to be able to assign keyboard press callbacks
* Camera
  * Possibly allow multiple cameras (think split screen games)
* Android Platform
  * Implement

-----------------------------------------------------------------------------------------------------------------------
