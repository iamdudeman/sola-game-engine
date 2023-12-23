# sola-game-engine TODO list

Rough order
1. ~~Text or image implementation to prove out layout basics~~
2. ~~Load from file implementation (little UI with refresh button from file for easier testing)~~
3. ~~Finish hooking up layout properties~~
4. ~~Implement "absolute" positioning where it is relative to parent but otherwise excluded from layout~~
5. Add other GuiElements + json blueprints
6. Switch events to have each child check if it "captures" the event (needed for absolute positioning)
7. Add "id" to elements and ability to getElementById (and method to add events after loading gui from json)
    * could also have an "event id -> event function" map that can be used as part of gui doc loading
    * elements could then add event listeners from this map
    * ie. (`mousePress` -> `doSomething`)
8. Make a "cookbook" to play around with properties and elements

Later things
* Consider event filters for elements to eliminate unneeded "bound" checks (mostly for mouse events)
    * maybe just check for any mouse event list size > 0?
* maybe put default style values in a common place?
* when should layout be invalidated (can partial invalidations happen for children/parent)
* ensure transparency stuff is hooked up properly
* hover properties
* "active" properties (apply style when clicking for example)

* Flex only layouts
    * no margin
    * gap
    * Visibility - none, hidden, visible
    * Alignment
        * HorizontalAlignment - left, right, center
        * VerticalAlignment - top, center, bottom
        * or alignMainAccess + alignCrossAccess?
    * direction - row, row-reverse, column, column-reverse

* GUI V2 implementation
    * "style" attribute
        * "layout" options
        * "paint" options
    * any element can have children passed in (but doesn't have to use them)
    * inherit styles from parent? (NO)
    * root "element" of document is full width and height by default and is always defined?
* ~~Don't force setting props for containers when creating them via gui document~~
* v1 bugs
    * stopping gui mouse event propagation currently isn't hooked up
        * this needs to be added to `GuiElementContainer#handleMouseEvent`
* ~~SolaGui~~
    * ~~Implement a way to load gui stuff from a file~~
        * ~~possibly use JSON to define gui structure~~
    * ~~A "post load" callback to add event listeners and such~~
* gui v2 later
    * absolute positioning
    * space-between, space-around, space-evenly

## Known Bugs List

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

* Unit Testing
    * Add lots of missing tests :)
* JavaDocs
    * add missing JavaDocs

-----------------------------------------------------------------------------------------------------------------------

## TODO List

* add "getId" to Asset
* update to Java 21
    * teavm 0.9.0 has support
* JSON schema files for
    * GuiV2 stuff
    * font stuff
    * spritesheet stuff
* Consider array list for possible SpacialHashMap performance boost
* Consider "stopping" AudioClip when it is finished by default, so it can be immediately played again?
* Move custom build "distribution" tasks to a gradle plugin
    * sola game template should use these plugins as well
    * maybe have all distributable files go to the same parent folder for convenience?
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
