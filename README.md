# TODO List
* AssetLoader
  * Rename to AssetPool or something like that
  * Remove File IO calls from AssetLoader
* Browser Platform
  * Write program to export Main into a JS file instead of gradle plugin
* Input
  * Mouse input handling
* Camera
  * Figure out how to implement
    * Translate
    * Scale / zoom
* Audio
  * Interface for type
  * Implement loader per platform (Swing and JavaFX can probably share though)
* GUI
  * Text?
  * Button
* Automated build pipeline
* Scripting language of some sort
  * How will that work with browser implementation?
* Physics
  * Rotation
  * Collision Detection
    * Collision layers / tags for ignoring specific collisions
    * Polygon x AABB
    * Polygon x Circle
    * Polygon x Polygon
* Particle System?
  * position
  * velocity
  * time left alive
  * color?
  * size?
* Engine GUI Editor (SolKana Editor)
  * Pass in instance of SolKana to start it up
    * Each game can have its own editor
    * This code could be a separate jar file with simple startup
  * Way to view all Systems and Components available
    * Components could use annotations for specifying editable field UIs
  * Way to view all Services available
  * Way to view various EventSubscribers
  * GameObject editor?
    * primitive types could generate a dialog of some sort
    * saves to json file
  * could load in multiple files
    * edit configurations go to appropriate files
  * play mode for quick testing
  * export stuff somehow maybe?
  * GUI Asset manager
    * Allows adding images and via file selector
    * Game has a project folder structure to add/remove files
  * Reads all registered Sols and allows adding to gameobjects
  * Uses all registered Kanas
  * Develop run mode that watches files and reloads on change
* Research any Steam integrations that can be done?
  * include JRE in the output file
    * might vary per platform
* Matrix2D
  * Implement inverse
  * Implement reflection
  * Implement orthogonal projection
