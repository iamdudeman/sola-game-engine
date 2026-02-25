# Editor TODO List

* Scene asset editor
    * UI Overview
        * Left pane is list of entities
            * ~~ability to rename entity~~
            * current scene toolbar in left panel
                * create scene
                * load scene (should load previously opened scene first)
                * add ability to switch scenes
                * ability to save scene
        * ~~Center is placeholder for now~~
        * ~~Right is info about selected entity components, editable~~
    * ~~Initial components~~
        * ~~TransformComponent~~
        * ~~CameraComponent~~
        * ~~CircleRendererComponent~~
    * ~~Comes with a default configuration for built-in components~~
        * ~~JsonMappers for components~~
        * ~~component editor modules for scene editor~~
    * Tool config
        * remember the last opened scene
        * remember the last selected entity

## Known Bugs List

* Deleting a folder that contains files with opened tabs won't clean up the opened tabs
* If files have the same name within an asset folder, but different extensions, there is no way to tell them apart

-----------------------------------------------------------------------------------------------------------------------

## Planned Cleanup List

*

-----------------------------------------------------------------------------------------------------------------------

## Editor TODO Notes

* Scene asset editor
    * disable/enable action needed (in left panel Entity tree)
    * Create child + parent relationships for the entities? (TransformComponent relationships?)
    * center panel for viewing current scene and starting a play preview
        * editor camera improvements
            * add new entities to center of editor camera
            * better camera controls than WSAD
            * option to focus editor camera on selected entity?
            * option to copy camera's transform for editor preview of it?
            * maybe show collider outline when collider component is active in editor?
                * if not this a toggle to view all collider outlines
    * additional components to support by default:
        * Physics
            * ParticleEmitterComponent
            * ColliderComponent
            * DynamicBodyComponent
        * Graphics
            * SpriteAnimatorComponent
            * TransformAnimatorComponent
            * BlendModeComponent
            * ~~CircleRendererComponent~~
            * ConvexPolygonRendererComponent
            * LayerComponent
            * LightComponent
            * RectRendererComponent
            * SpriteComponent
            * TriangleRendererComponent
        * ~~TransformComponent~~
        * ~~CameraComponent~~
    * UI for creating and previewing ParticleEmitterComponent configurations
        * (might be better to start working on the Scene composition instead since this nests in it)
        * serialize the component and its parts to JSON
        * The left panel can select the existing configuration or create new
        * the right panel shows emitter details

* assets tooling
    * create and edit materials
    * create and edit GUIs
        * maybe a WYSIWYG editor?

-----------------------------------------------------------------------------------------------------------------------
