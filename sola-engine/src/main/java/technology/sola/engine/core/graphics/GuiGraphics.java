package technology.sola.engine.core.graphics;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.ecs.SolaEcs;
import technology.sola.ecs.Entity;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.components.LayerComponent;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.graphics.gui.components.GuiPanelComponent;
import technology.sola.engine.graphics.gui.components.GuiTextComponent;

class GuiGraphics {
  static void render(Renderer renderer, SolaEcs solaEcs, AssetPool<Font> fontAssetPool) {
    // Gui panels
    solaEcs.getWorld().findEntitiesWithComponents(TransformComponent.class, GuiPanelComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderGuiPanels(renderer, entity);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderGuiPanels(renderer, entity));
        }
      });

    // Gui text
    solaEcs.getWorld().findEntitiesWithComponents(TransformComponent.class, GuiTextComponent.class)
      .forEach(entity -> {
        LayerComponent layerComponent = entity.getComponent(LayerComponent.class);

        if (layerComponent == null) {
          renderGuiText(renderer, entity, fontAssetPool);
        } else {
          renderer.drawToLayer(layerComponent.getLayer(), layerComponent.getPriority(), r -> renderGuiText(renderer, entity, fontAssetPool));
        }
      });
  }

  private static void renderGuiPanels(Renderer renderer, Entity entity) {
    TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
    GuiPanelComponent guiPanelComponent = entity.getComponent(GuiPanelComponent.class);

    if (guiPanelComponent.getBackgroundColor().hasAlpha()) {
      renderer.setRenderMode(RenderMode.ALPHA);
    }
    renderer.fillRect(transformComponent.getX(), transformComponent.getY(), transformComponent.getScaleX(), transformComponent.getScaleY(), guiPanelComponent.getBackgroundColor());
    renderer.setRenderMode(RenderMode.NORMAL);

    if (guiPanelComponent.getBorderColor() != null) {
      if (guiPanelComponent.getBorderColor().hasAlpha()) {
        renderer.setRenderMode(RenderMode.ALPHA);
      }
      renderer.drawRect(transformComponent.getX(), transformComponent.getY(), transformComponent.getScaleX(), transformComponent.getScaleY(), guiPanelComponent.getBorderColor());
      renderer.setRenderMode(RenderMode.NORMAL);
    }
  }

  private static void renderGuiText(Renderer renderer, Entity entity, AssetPool<Font> fontAssetPool) {
    TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
    GuiTextComponent guiTextComponent = entity.getComponent(GuiTextComponent.class);

    if (guiTextComponent.getFontAssetId() == null || guiTextComponent.getText() == null) return;

    renderer.setFont(guiTextComponent.getFont(fontAssetPool));

    renderer.drawWithRenderModeMask(r -> renderer.drawString(guiTextComponent.getText(), transformComponent.getX(), transformComponent.getY(), guiTextComponent.getColor()));
  }
}
