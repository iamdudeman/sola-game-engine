# sola-game-engine TODO list

## Known Bugs List

* Fix default dark theme button disabled color
* When GUI direction is row or column reverse then the keyboard shortcuts should also be reversed
* UI flicker on Android. Might be related to not having hover in and out. Seems like it sometimes gets stuck in a layout update.
* (verify if still bug) GuiElement parent undefined when changing roots
    * consider creating a new RootGuiElement when changing roots instead of clearing out root children
        * if this works then can remove parent == null checks
* (warning) Unsupported JavaFX configuration: classes were loaded from 'unnamed module @47c62251'
    * https://stackoverflow.com/questions/67854139/javafx-warning-unsupported-javafx-configuration-classes-were-loaded-from-unna
    * https://stackoverflow.com/questions/76155054/javafx-installation
    * https://openjfx.io/openjfx-docs/#gradle

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

* Add `public abstract ElementType self();` to GuiElement to cleanup unchecked annotations

-----------------------------------------------------------------------------------------------------------------------

## TODO List

* Add ability to toggle to hide all GUI
* Maybe add a concept some sort of "navigation" for Gui stuff?
    * include a "navigate" method where a "payload" of some sort can be passed in
    * `navigate<T>(String path, T payload)`
    * add interface for `NavTarget<Payload>` with a `String path()` method and `onNavigate(Payload payload)`
* Update examples to work off of mouse/touch only
    * cleanup logic in `ExampleLauncherSola#addReturnToLauncherKeyEvent`
* implement the Android platform fully
    * keyboard input is not fully implemented
        * AndroidSolaPlatform#mapKeyCode does not support all key codes!
* Figure out how to handle TouchInput
    * Primarily for browser and Android but also could be supported in JavaFX
    * touchstart and touchend all hard coded to MouseButton.Primary currently
        * implement touchmove
            * Figure out TouchInput API
        * How to handle multitouch
            * Switch JsMouseUtils to mouse events instead of pointer events
* Add convenient ability to change entity render order based on Y position (or some other condition other than layers)
* Consider generalizing Triangle renderer and collider to "Polygon" instead
* research possible benefits of updating to Java 21
    * teavm 0.9.0 has support now
* Support .mp3 audio files for all platforms
* File Storage API
    * Load and save JSON content (maybe other content too)
    * Browser implementation could open file dialog
    * Desktop could go straight to file or open file dialog
* Investigate possible Steam CloudSave integration
* Rendering
    * Ability to change line width when drawing
    * Add a way to do gradients?
        * maybe just linear at first?
        * maybe just a convenience method for generating gradient SolaImages?
* Gui performance improvements
    * when should layout be invalidated (can partial invalidations happen for children/parent)
    * consider splitting layout and paint styles to prevent extra calculations
* Implement "grow" concept for GuiElement children
    * grow and shrink
* REST client + server
    * ability to send and process Authorization header
* Lighting
    * implement more light types other than just point lights
    * https://www.redblobgames.com/articles/visibility/
    * https://www.roguebasin.com/index.php?title=Field_of_Vision
* Particle System
    * consider ability to add fix number of particle spawns (4 at a time in different directions for example)
    * consider ability to change particle shape (instead of only circle maybe square)
    * consider researching how other engines do particle systems to maybe create an easier to use api
    * consider adding acceleration
    * consider some sort of "swaying" for non-linear particles
    * consider ability to spawn particles in a radius away from center
        * probably want the ability to make particles go to and from center for this
* Gui json features
    * consider an event map of some sort for when loading gui documents
        * could also have an "event id -> event function" map that can be used as part of gui doc loading
        * elements could then add event listeners from this map
        * ie. (`mousePress` -> `doSomething`)
    * implement "Style sheet" JSON concept
        * gui json documents can reference these for use
* Consider adding rotation to TransformComponent
    * Would need to update rendering stuff
    * Would need to update physics stuff
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
