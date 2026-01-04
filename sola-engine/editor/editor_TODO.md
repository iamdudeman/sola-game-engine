# Editor TODO List

*

## Known Bugs List

* Deleting a folder that contains files with opened tabs won't clean up the opened tabs
* If files have the same name within an asset folder, but different extensions, there is no way to tell them apart

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

*

-----------------------------------------------------------------------------------------------------------------------

## Editor TODO Notes

Comes with default configuration for built-in components and systems

* assets tooling
    * create and edit materials
    * create and edit GUIs
        * maybe a WYSIWYG editor?

* UI for creating and previewing ParticleEmitterComponent configurations
    * (might be better to start working on the Scene composition instead since this nests in it)
    * serialize the component and its parts to JSON
    * The left panel can select the existing configuration or create new
    * the right panel shows emitter details

* Editor instance configuration (composable) (goes with Scene composition)
    1. Register all EcsSystems
        1. UI for configuring them
    2. Register all Components
        * Has some sort of ui metadata to generate fields for updating the components on an entity
            * could maybe use annotations that the editor would then read
    3. Register all Graphics modules
        * UI for configuring them
    * might have a different "runtime" similar to SolaGraphics and SolaPhysics

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
