package technology.sola.engine.editor.core;

import technology.sola.engine.editor.ui.ecs.SolaEditorEntityComponentMenus;
import technology.sola.engine.editor.ui.ecs.general.TransformComponentController;
import technology.sola.engine.editor.ui.ecs.rendering.CircleRendererComponentController;
import technology.sola.engine.editor.ui.ecs.rendering.RectangleRendererComponentController;
import technology.sola.engine.editor.ui.ecs.rendering.gui.GuiPanelComponentController;

public class EditorConfiguration {
  private final SolaEditorEntityComponentMenus solaEditorEntityComponentMenus;

  public EditorConfiguration() {
    solaEditorEntityComponentMenus = new SolaEditorEntityComponentMenus();

    solaEditorEntityComponentMenus.addMenu("General")
      .addItem("Transform", new TransformComponentController());
    solaEditorEntityComponentMenus.addMenu("Rendering")
      .addItem("Rectangle", new RectangleRendererComponentController())
      .addItem("Circle", new CircleRendererComponentController())
      .addSubMenu("Gui")
      .addItem("Panel", new GuiPanelComponentController());
    solaEditorEntityComponentMenus.addMenu("Physics");
  }

  public SolaEditorEntityComponentMenus getEntityComponentMenus() {
    return solaEditorEntityComponentMenus;
  }
}
