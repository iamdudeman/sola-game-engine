# Branch WIP
* Bug discovered in JavaFxMain when doing PlatformerExample and getting to the end from the side
  * Exception in thread "JavaFX Application Thread" java.util.ConcurrentModificationException
    at java.base/java.util.ArrayList.forEach(ArrayList.java:1513)
    at technology.sola.engine.event.EventHub.lambda$emit$3(EventHub.java:34)
    at java.base/java.util.HashMap.computeIfPresent(HashMap.java:1261)
    at technology.sola.engine.event.EventHub.emit(EventHub.java:33)
    at technology.sola.engine.physics.system.CollisionDetectionSystem.lambda$update$1(CollisionDetectionSystem.java:85)
    at java.base/java.lang.Iterable.forEach(Iterable.java:75)
    at technology.sola.engine.physics.system.CollisionDetectionSystem.update(CollisionDetectionSystem.java:85)
    at technology.sola.ecs.SolaEcs.lambda$updateWorld$0(SolaEcs.java:58)
    at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:179)
    at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
    at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
    at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
    at java.base/java.util.stream.StreamSpliterators$WrappingSpliterator.forEachRemaining(StreamSpliterators.java:310)
    at java.base/java.util.Spliterators$1Adapter.forEachRemaining(Spliterators.java:706)
    at technology.sola.ecs.SolaEcs.updateWorld(SolaEcs.java:58)
    at

-----------------------------------------------------------------------------------------------------------------------

## Bugs List

-----------------------------------------------------------------------------------------------------------------------

## Cleanup List

-----------------------------------------------------------------------------------------------------------------------

## TODO List

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

-----------------------------------------------------------------------------------------------------------------------
