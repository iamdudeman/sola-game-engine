# sola-game-engine TODO list

## Known Bugs List

* stopping gui mouse event propagation currently isn't hooked up
    * this needs to be added to `GuiElementContainer#handleMouseEvent`
* Exception in thread "Thread-3" technology.sola.engine.assets.exception.MissingAssetException: Asset with id [rememory.png] does not exist
  at technology.sola.engine/technology.sola.engine.assets.AssetLoader.get(AssetLoader.java:65)
  at technology.sola.engine/technology.sola.engine.assets.AssetLoader.getNewAsset(AssetLoader.java:88)
  at technology.sola.engine.platform.javafx@4d0fd62f4f/technology.sola.engine.platform.javafx.assets.JavaFxSpriteSheetAssetLoader.lambda$loadAsset$2(JavaFxSpriteSheetAssetLoader.java:44)
  at java.base/java.lang.Thread.run(Thread.java:833)


-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

* Change `SolaEntityGraphicsModule<View1Entry<SomeComponent>>` to just `SolaEntityGraphicsModule<SomeComponent>` if possible
* Don't force setting props for containers when creating them via gui document
* Unit Testing
    * Add lots of missing tests :)
* JavaDocs
    * add missing JavaDocs

-----------------------------------------------------------------------------------------------------------------------

## TODO List

* Move custom build "distribution" tasks to a gradle plugin
    * sola game template should use these plugins as well
* Consider mechanism to "batch" together SolaGraphics entity draws when they are in the same layer
    * Currently, it does each one individually
* Rendering
    * Ability to change line width when drawing
    * Add a way to do gradients?
        * maybe just linear at first?
* networking
    * rest client interface
        * javafx/swing impl
        * browser impl
    * simple rest server
* Lighting
    * implement more light types other than just point lights
* Particle System
    * consider adding acceleration
    * consider some sort of "swaying" for non-linear particles
    * consider ability to spawn particles in a radius away from center
        * probably want the ability to make particles go to and from center for this
* SolaGui
    * Implement a way to load gui stuff from a file
        * possibly use JSON to define gui structure
    * A "post load" callback to add event listeners and such
* Consider adding rotation to TransformComponent
    * Would need to update rendering stuff
    * Would need to update physics stuff
* Physics
    * Collision Detection
        * Polygon x AABB
        * Polygon x Circle
        * Polygon x Polygon
* Figure out how to handle TouchInput (probably at the same time as AndroidPlatform implementation)
    * Primarily for browser but also could be supported in JavaFX
    * touchstart and touchend all hard coded to MouseButton.Primary currently
        * implement touchmove
            * Figure out TouchInput API
        * How to handle multitouch
            * Switch JsMouseUtils to mouse events instead of pointer events
* Android Platform
    * Implement
* Consider adding a "debug console" option
    * While open can toggle things like render debug outlines and debug spacial hashmap stuff
    * Could also maybe allow adding custom commands
    * Should probably always use a "default font" if it is implemented
    * Possibly also have ability to show light maps if lighting is enabled
* Browser Platform
    * modularize (requires figuring out how to modularize parts of teavm needed)
        * ensure `SimpleSolaBrowserFileServer` and `SolaBrowserFileBuilder` in `tools` is exposed as well
    * Improve performance (StressTestExample can't handle a lot of objects)
        * Possibly finish implementing BrowserCanvasRenderer?
    * Consider web worker for game loop
        * main thread creates needed dom events
        * mouse and key events sent to worker
        * worker sends ImageData to main thread to render
        * (this approach may improve performance for StressTestExample to work better)
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

-----------------------------------------------------------------------------------------------------------------------
