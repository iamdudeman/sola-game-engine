# sola-game-engine TODO list

## Known Bugs List
* When SpriteComponent used with TransformComponent that has scale of 0 an exception is thrown
  * Exception in thread "Thread-4" java.lang.IllegalArgumentException: max.x cannot be less than min.x
    at technology.sola.engine/technology.sola.math.geometry.Rectangle.<init>(Rectangle.java:22)
    at technology.sola.engine/technology.sola.math.linear.Matrix3D.getBoundingBoxForTransform(Matrix3D.java:142)
    at technology.sola.engine/technology.sola.engine.graphics.AffineTransform.getBoundingBoxForTransform(AffineTransform.java:44)
    at technology.sola.engine/technology.sola.engine.graphics.renderer.SoftwareRenderer.drawImage(SoftwareRenderer.java:194)
    at technology.sola.engine/technology.sola.engine.core.module.graphics.SpriteGraphics.lambda$renderSprite$1(SpriteGraphics.java:47)
    at technology.sola.engine/technology.sola.engine.assets.AssetHandle.executeIfLoaded(AssetHandle.java:49)
    at technology.sola.engine/technology.sola.engine.core.module.graphics.SpriteGraphics.renderSprite(SpriteGraphics.java:36)
    at technology.sola.engine/technology.sola.engine.core.module.graphics.SpriteGraphics.lambda$render$0(SpriteGraphics.java:23)
    at technology.sola.engine/technology.sola.engine.graphics.renderer.Layer$PrioritizedDrawItem.draw(Layer.java:58)
    at technology.sola.engine/technology.sola.engine.graphics.renderer.Layer.draw(Layer.java:33)
    at technology.sola.engine/technology.sola.engine.core.SolaPlatform.render(SolaPlatform.java:123)
    at technology.sola.engine/technology.sola.engine.core.SolaPlatform.lambda$initComplete$2(SolaPlatform.java:100)
    at technology.sola.engine/technology.sola.engine.core.FixedUpdateGameLoop.run(FixedUpdateGameLoop.java:37)
    at java.base/java.lang.Thread.run(Thread.java:833)

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

-----------------------------------------------------------------------------------------------------------------------

## TODO List

* Rendering
  * Ability to change line width when drawing
  * Implement more BlendModes
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
* Consider some networking stuff
  * Socket Clients would need an interface to work off of
    * implement for Swing and JavaFX
    * implement for Browser (socket.io maybe)
  * Socket Server could maybe use a "console" SolaPlatform
    * or maybe the Server can "play" a Sola but is something custom?
  * REST client as well (some interface)
    * implement for Swing and JavaFX
    * implement for Browser
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
