# Editor TODO List

## Known Bugs List

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

-----------------------------------------------------------------------------------------------------------------------


## Editor TODO List

Creates json files to assets directory

Composable editor pieces
* Component GUI
* Systems GUI
* Game UI GUI

Comes with default pieces and can extend them

* setup
    1. Register all EcsSystems
        1. UI for configuration them
    2. Register all Components
    3. Register all Graphics modules
    4. Register all GuiElements

* Scene composition
    * EcsSystems initially enabled
    * Graphics modules initially enabled
    * World of initial entities
    * Initial GuiElement root

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

-----------------------------------------------------------------------------------------------------------------------
