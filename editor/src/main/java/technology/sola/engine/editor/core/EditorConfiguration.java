package technology.sola.engine.editor.core;

import technology.sola.engine.editor.ui.ecs.SolaEditorEntityComponentMenus;
import technology.sola.engine.editor.ui.ecs.general.CameraComponentController;
import technology.sola.engine.editor.ui.ecs.general.TransformComponentController;
import technology.sola.engine.editor.ui.ecs.physics.DynamicBodyComponentController;
import technology.sola.engine.editor.ui.ecs.rendering.CircleRendererComponentController;
import technology.sola.engine.editor.ui.ecs.rendering.LayerComponentController;
import technology.sola.engine.editor.ui.ecs.rendering.RectangleRendererComponentController;
import technology.sola.engine.editor.ui.ecs.rendering.gui.GuiPanelComponentController;

public class EditorConfiguration {
  private final SolaEditorEntityComponentMenus solaEditorEntityComponentMenus;

  public EditorConfiguration(SolaEditorContext solaEditorContext) {
    solaEditorEntityComponentMenus = new SolaEditorEntityComponentMenus();

    solaEditorEntityComponentMenus.addMenu("General")
      .addItem("Transform", new TransformComponentController(solaEditorContext))
      .addItem("Camera", new CameraComponentController(solaEditorContext));
    solaEditorEntityComponentMenus.addMenu("Rendering")
      .addItem("Rectangle", new RectangleRendererComponentController(solaEditorContext))
      .addItem("Circle", new CircleRendererComponentController(solaEditorContext))
      .addItem("Layer", new LayerComponentController(solaEditorContext))
      .addSubMenu("Gui")
        .addItem("Panel", new GuiPanelComponentController(solaEditorContext));
    solaEditorEntityComponentMenus.addMenu("Physics")
      .addItem("Dynamic Body", new DynamicBodyComponentController(solaEditorContext));
  }

  public SolaEditorEntityComponentMenus getEntityComponentMenus() {
    return solaEditorEntityComponentMenus;
  }
}
