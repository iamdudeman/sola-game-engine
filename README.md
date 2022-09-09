# Branch WIP

-----------------------------------------------------------------------------------------------------------------------

## Bugs List
* browser fat jar not working without consuming project also defining a couple teavm dependencies
  * runtimeOnly("org.teavm:teavm-classlib:0.7.0")
    runtimeOnly("org.teavm:teavm-extras-slf4j:0.7.0")
* Figure out proper fat jar setup so stuff isn't duplicated
* Investigate JavaFx library bundling (api vs implementation?)
* When browser tab loses focus the game can run in an indeterminate state
  * Probably need some sort of safe "pause" functionality (maybe used when games are minimized in desktop as well
* investigate performance hits when moving mouse on browser canvas
  * shouldn't cause slowdowns
* audio stuff not running sometimes https://developer.chrome.com/blog/autoplay/#webaudio

-----------------------------------------------------------------------------------------------------------------------

## Cleanup List

-----------------------------------------------------------------------------------------------------------------------

## TODO List
* Consider creating a Point2D class to use in cases where Vector2D is not needed
* Ability to add offset for colliders
* Figure out touch events for browser
  * touchstart end and move all hard coded to MouseButton.Primary
  * How to handle multitouch
  * Should it have its own TouchInput instead of doing it with MouseInput?
* Ability to ignore certain collision types
  * Tiles may be collidable but if they don't move at all don't check for collisions with each other
* Assets
  * Consider adding JsonAsset and JsonAssetLoader
  * Consider implementing generic SpriteSheetAssetLoader and generic FontAssetLoader utilizing platform specific
    JsonAssetLoader and SolaImageAssetLoader
    * Maybe pass AssetLoaderProvider to each AssetLoader instance to make this easier
* Consider adding a "debug console"
  * While open can toggle things like render debug outlines and debug spacial hashmap stuff
  * Could also maybe allow adding custom commands
  * Should probably always use a "default font" if it is implemented
* Rendering
  * SolaGraphics could cull entities that are outside the camera viewport
  * More BlendMode implementations
* SolaGui Stuff
  * GuiElementContainer
    * Consider adding anchor support to Stream or a new container to be able to easily center things?
* Add tooling project back
  * Could have a gradle task example for each tool similar to generateBrowserExampleHtmlAndJs?
  * Consider adding tool to rasterize fonts
  * Research build tooling of some sort
    * Take a main java file and build for a platform maybe?
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
* Research Virtual File System
* Build pipeline
  * Generate engine and platform artifacts
  * sources and javadoc jar
  * use github pages and "Javadoc deploy" action for hosting javadocs
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
