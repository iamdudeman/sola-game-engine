package technology.sola.engine.core.graphics;

import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.ecs.EcsSystemContainer;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.gui.components.GuiPanelComponent;

class GuiGraphics {
  static void render(Renderer renderer, EcsSystemContainer ecsSystemContainer) {
    ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class, GuiPanelComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderGuiPanels(renderer, entity);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderGuiPanels(renderer, entity));
        }
      });
  }

  private static void renderGuiPanels(Renderer renderer, Entity entity) {
    // todo transform needs to account for parent values as well if present
    TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
    GuiPanelComponent guiPanelComponent = entity.getComponent(GuiPanelComponent.class);

    if (guiPanelComponent.getBackgroundColor().hasAlpha()) {
      renderer.setRenderMode(RenderMode.ALPHA);
    }

    renderer.fillRect(transformComponent.getX(), transformComponent.getY(), transformComponent.getScaleX(), transformComponent.getScaleY(), guiPanelComponent.getBackgroundColor());

    renderer.setRenderMode(RenderMode.NORMAL);
  }
}
