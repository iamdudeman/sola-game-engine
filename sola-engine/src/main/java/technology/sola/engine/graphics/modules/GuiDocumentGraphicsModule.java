package technology.sola.engine.graphics.modules;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.World;
import technology.sola.engine.graphics.gui.GuiDocument;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;

/**
 * SolaGraphicsModule for rendering a {@link GuiDocument}. This will render on top
 * of {@link technology.sola.engine.graphics.renderer.Layer}s if they are present.
 */
@NullMarked
public class GuiDocumentGraphicsModule extends SolaGraphicsModule {
  private final GuiDocument guiDocument;

  /**
   * Creates an instance of this graphics module.
   *
   * @param guiDocument the {@link GuiDocument} to render
   */
  public GuiDocumentGraphicsModule(GuiDocument guiDocument) {
    this.guiDocument = guiDocument;
  }

  @Override
  public void renderMethod(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    var layers = renderer.getLayers();

    if (layers.isEmpty()) {
      guiDocument.render(renderer);
    } else {
      layers.get(layers.size() - 1).add(guiDocument::render, RenderOrders.GUI);
    }
  }

  @Override
  public int getOrder() {
    return RenderOrders.GUI;
  }
}
