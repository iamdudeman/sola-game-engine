# sola-game-engine TODO list

## Known Bugs List

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

-----------------------------------------------------------------------------------------------------------------------

## TODO List

* research possible benefits of updating to Java 21
    * teavm 0.9.0 has support now
* Gui improvements
    * when should layout be invalidated (can partial invalidations happen for children/parent)
    * consider splitting layout and paint styles to prevent extra calculations
    * consider an event map of some sort for when loading gui documents
        * could also have an "event id -> event function" map that can be used as part of gui doc loading
        * elements could then add event listeners from this map
        * ie. (`mousePress` -> `doSomething`)
    * implement "Style sheet" JSON concept
        * gui json documents can reference these for use
* research why browser collision detection performance is so much worse
* consider making QuadTreeBroadPhase the default
* Rendering
    * Ability to change line width when drawing
    * Add a way to do gradients?
        * maybe just linear at first?
        * maybe just a convenience method for generating gradient SolaImages?
* networking
    * rest client interface
        * javafx impl
        * swing impl
        * browser impl
    * simple rest server
* Figure out how to handle TouchInput (probably at the same time as AndroidPlatform implementation)
    * Primarily for browser but also could be supported in JavaFX
    * touchstart and touchend all hard coded to MouseButton.Primary currently
        * implement touchmove
            * Figure out TouchInput API
        * How to handle multitouch
            * Switch JsMouseUtils to mouse events instead of pointer events
* Android Platform
    * Implement
* Unit Testing
    * Add lots of missing tests :)
* Investigate possible Steam CloudSave integration
* Lighting
    * implement more light types other than just point lights
* Particle System
    * consider adding acceleration
    * consider some sort of "swaying" for non-linear particles
    * consider ability to spawn particles in a radius away from center
        * probably want the ability to make particles go to and from center for this
* Consider adding rotation to TransformComponent
    * Would need to update rendering stuff
    * Would need to update physics stuff
* Physics
    * Collision Detection
        * Polygon x AABB
        * Polygon x Circle
        * Polygon x Polygon
* Consider adding a "debug console" option
    * While open can toggle things like render debug outlines and debug spatial hashmap stuff
    * Could also maybe allow adding custom commands
    * Should probably always use a "default font" if it is implemented
    * Possibly also have ability to show light maps if lighting is enabled
* Browser Platform
    * Keep an eye on Cheerpj for the future of browser stuff (it doesn't support Java 17+ yet)
        * https://labs.leaningtech.com/cheerpj3/getting-started/Java-app
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
