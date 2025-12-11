# sola-game-engine TODO list

## Known Bugs List

* AndroidPlatform request keyboard doesn't seem to work

### Low-priority bugs

* (warning) Unsupported JavaFX configuration: classes were loaded from 'unnamed module @47c62251'
    * https://stackoverflow.com/questions/67854139/javafx-warning-unsupported-javafx-configuration-classes-were-loaded-from-unna
    * https://stackoverflow.com/questions/76155054/javafx-installation
    * https://openjfx.io/openjfx-docs/#gradle
* Android: Edge-to-edge may not display for all users
    * > From Android 15, apps targeting SDK 35 will display edge-to-edge by default. Apps targeting SDK 35 should handle insets to make sure that their app displays correctly on Android 15 and later. Investigate this issue and allow time to test edge-to-edge and make the required updates. Alternatively, call enableEdgeToEdge() for Kotlin or EdgeToEdge.enable() for Java for backward compatibility.
* Android: Your app uses deprecated APIs or parameters for edge-to-edge
    * > One or more of the APIs you use or parameters that you set for edge-to-edge and window display have been deprecated in Android 15. Your app uses the following deprecated APIs or parameters:
      > android.view.Window.setStatusBarColor
      > android.view.Window.setNavigationBarColor
      > LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
      > These start in the following places:
      > androidx.activity.q.a
      > androidx.activity.p.b
      > To fix this, migrate away from these APIs or parameters.
* Android: Remove resizability and orientation restrictions in your game to support large screen devices
    * > Your game doesn't support all display configurations, and uses resizability and orientation restrictions that may lead to layout issues for your users.
      > We detected the following resizability and orientation restrictions in your game:
      > e2.c.onCreate
      > To improve the user experience of your game, remove these restrictions and check that your game layouts work on various screen sizes and orientations.

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

* deprecated gradle features `:examples:android`
    * Declaring dependencies using multi-string notation has been deprecated. This will fail with an error in Gradle 10. Please use single-string notation instead: "com.android.tools.lint:lint-gradle:31.12.3". Consult the upgrading guide for further information: https://docs.gradle.org/9.1.0/userguide/upgrading_version_9.html#dependency_multi_string_notation
    * Declaring dependencies using multi-string notation has been deprecated. This will fail with an error in Gradle 10. Please use single-string notation instead: "com.android.tools.build:aapt2:8.12.3-13700139:windows". Consult the upgrading guide for further information: https://docs.gradle.org/9.1.0/userguide/upgrading_version_9.html#dependency_multi_string_notation

-----------------------------------------------------------------------------------------------------------------------

## TODO List

* Android performance improvements
    * Example launcher shouldn't use much cpu
    * PhysicsExample should be able to run better
* Particle System
    * consider ability to add fix number of particle spawns (4 at a time in different directions for example)
    * consider ability to change particle shape (instead of only circle maybe square)
    * consider researching how other engines do particle systems to maybe create an easier to use api
    * consider adding acceleration
    * consider some sort of "swaying" for non-linear particles
    * consider ability to spawn particles in a radius away from center
        * probably want the ability to make particles go to and from center for this

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
    * Improve performance (StressTestExample can't handle a lot of objects)
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
