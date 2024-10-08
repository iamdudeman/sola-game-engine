# Editor TODO List

## Known Bugs List

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

-----------------------------------------------------------------------------------------------------------------------

## Editor TODO Notes

Comes with default configuration for built-in components and systems

* Editor instance configuration (composable)
    1. Register all EcsSystems
        1. UI for configuring them
    2. Register all Components
        * Has some sort of ui metadata to generate fields for updating the components on an entity
    3. Register all Graphics modules
        * UI for configuring them

* assets tooling
    * create and edit font assets
    * create and edit controls assets
    * file browser in main UI to see assets
        * can maybe be used to update components' values later that consume asset ids
    * create and edit materials
    * create and edit spritesheets
    * create and edit GUIs
        * maybe a WYSIWYG editor?

* Scene composition
    * UI Overview
        * Left pane is list of entities
        * Center is preview of entities
        * Right is info about selected entity
        * Bottom is assets maybe?
    * EcsSystems initially enabled
    * Graphics modules initially enabled
    * Way to view various EventListeners?
    * World of initial entities
    * Initial GuiElement root
    * editor camera improvements
        * add new entities to center of editor camera
        * better camera controls than WSAD
        * option to focus editor camera on selected entity?
        * option to copy camera's transform for editor preview of it?
        * maybe show collider outline when collider component is active in editor?
            * if not this a toggle to view all collider outlines
    * Create child + parent relationships for the entities?

-----------------------------------------------------------------------------------------------------------------------
