# sola-game-engine TODO list

* Particle System
    * consider researching how other engines do particle systems to maybe create an easier to use api
        * unity adds particle functionality via modules (ex. "Color over lifetime module")
    * appearance
        * modify size over lifetime option
        * modify size by velocity
    * movement
        * inherit velocity from Entity
        * modify velocity over lifetime
        * consider some sort of "swaying" for non-linear particles
            * concept of "noise" for movement - https://docs.unity3d.com/Manual/PartSysNoiseModule.html
    * emission
        * consider ability to spawn particles in a radius away from center
            * probably want the ability to make particles go to and from center for this
    * consider adding support for "pooling" for potential performance improvement

## Known Bugs List

*

### Low-priority bugs

*

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

* deprecated gradle features `:examples:android`
    * Declaring dependencies using multi-string notation has been deprecated. This will fail with an error in Gradle 10. Please use single-string notation instead: "com.android.tools.lint:lint-gradle:31.12.3". Consult the upgrading guide for further information: https://docs.gradle.org/9.1.0/userguide/upgrading_version_9.html#dependency_multi_string_notation
    * Declaring dependencies using multi-string notation has been deprecated. This will fail with an error in Gradle 10. Please use single-string notation instead: "com.android.tools.build:aapt2:8.12.3-13700139:windows". Consult the upgrading guide for further information: https://docs.gradle.org/9.1.0/userguide/upgrading_version_9.html#dependency_multi_string_notation

-----------------------------------------------------------------------------------------------------------------------

## TODO List

*

### Low priority (not ordered)

* support mp3 on `SwingSolaPlatform`
* ability to render ellipse (and collisions for ellipse)
* research possible benefits of updating to Java 21
    * teavm 0.9.0 has support now
    * Android does not have support yet
* research possible benefits of updating to Java 25
    * teavm 0.13 has support now
    * Android does not have support yet
* Consider adding rotation to TransformComponent
    * Would need to update rendering stuff
    * Would need to update physics stuff
        * Not just colliders, but also impulse collision resolution potentially
* Gui performance improvements
    * consider splitting layout and paint styles to prevent extra calculations
        * changing background color doesn't affect layout
        * changing border color doesn't affect layout `if a border is already set`
* Research Virtual File System
    * ability to mount archives of some sort
        * possible example, instead of png use different file format that many can be compressed into one larger file
            * path /test
            * contents 100 100 ffffffff 00ff00ff
* Investigate possible Steam integrations
    * CloudSave integration
    * Controls integration
* Gui json features
    * consider an event map of some sort for when loading gui documents
        * could also have an "event id -> event function" map that can be used as part of gui doc loading
        * elements could then add event listeners from this map
        * ie. (`mousePress` -> `doSomething`)
    * implement "Style sheet" JSON concept
        * gui json documents can reference these for use
* Rendering
    * Ability to change line width when drawing
    * Add a way to do gradients?
        * maybe just linear at first?
        * maybe just a convenience method for generating gradient SolaImages?
* Implement "grow" concept for GuiElement children
    * grow and shrink
* REST client + server
    * ability to send and process Authorization header
* Lighting
    * implement more light types other than just point lights
    * https://www.redblobgames.com/articles/visibility/
    * https://www.roguebasin.com/index.php?title=Field_of_Vision
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
    * Possibly finish implementing BrowserCanvasRenderer?
    * Consider web worker for game loop
        * main thread creates needed dom events
        * mouse and key events sent to worker
        * worker sends ImageData to main thread to render
        * (this approach may improve performance for StressTestExample to work better)
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
* Android keyboard input is not fully implemented
    * AndroidSolaPlatform#mapKeyCode does not support all key codes!
* Browser platform `setVirtualKeyboardVisible` support would be nice to have

-----------------------------------------------------------------------------------------------------------------------
