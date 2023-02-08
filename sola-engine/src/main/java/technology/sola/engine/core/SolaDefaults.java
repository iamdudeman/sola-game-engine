package technology.sola.engine.core;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.SolaGraphics;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.modules.CircleGraphicsModule;
import technology.sola.engine.graphics.modules.DebugGraphicsModule;
import technology.sola.engine.graphics.modules.RectangleGraphicsModule;
import technology.sola.engine.graphics.modules.SpriteGraphicsModule;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.SolaPhysics;
import technology.sola.engine.physics.system.CollisionDetectionSystem;

import java.util.function.Consumer;

public class SolaDefaults {
  private final Sola sola;
  private SolaPhysics physics;
  private SolaGraphics graphics;
  private SolaGuiDocument guiDocument;
  private Color clearColor = Color.BLACK;

  private boolean isDebug = false;
  private Consumer<Renderer> renderFunction = renderer -> {};

  SolaDefaults(Sola sola) {
    this.sola = sola;
  }

  void render(Renderer renderer) {
    renderFunction.accept(renderer);
  }

  public SolaDefaults useClearColor(Color clearColor) {
    this.clearColor = clearColor;
    rebuildRenderFunction();

    return this;
  }

  public SolaDefaults usePhysics() {
    if (physics != null) {
      return this;
    }

    this.physics = new SolaPhysics(sola.eventHub);

    sola.solaEcs.addSystems(this.physics.getSystems());

    if (isDebug && graphics != null) {
      graphics.getGraphicsModuleList().add(new DebugGraphicsModule(sola.solaEcs.getSystem(CollisionDetectionSystem.class)));
    }

    return this;
  }

  public SolaDefaults useGraphics() {
    return useGraphics(false);
  }

  public SolaDefaults useGraphics(boolean isDebug) {
    if (graphics != null) {
      return this;
    }

    this.isDebug = isDebug;
    this.graphics = new SolaGraphics(sola.solaEcs);

    sola.solaEcs.addSystems(graphics.getSystems());

    AssetLoader<SpriteSheet> spriteSheetAssetLoader = sola.assetLoaderProvider.get(SpriteSheet.class);

    graphics.getGraphicsModuleList().add(new CircleGraphicsModule());
    graphics.getGraphicsModuleList().add(new RectangleGraphicsModule());
    graphics.getGraphicsModuleList().add(new SpriteGraphicsModule(spriteSheetAssetLoader));

    if (isDebug && physics != null) {
      graphics.getGraphicsModuleList().add(new DebugGraphicsModule(sola.solaEcs.getSystem(CollisionDetectionSystem.class)));
    }

    rebuildRenderFunction();

    return this;
  }

  public SolaDefaults useGui() {
    if (guiDocument != null) {
      return this;
    }

    guiDocument = new SolaGuiDocument(sola.platform, sola.assetLoaderProvider, sola.eventHub);

    rebuildRenderFunction();

    return this;
  }

  public SolaGuiDocument getGuiDocument() {
    return guiDocument;
  }

  private void rebuildRenderFunction() {
    if (graphics == null) {
      renderFunction = renderer -> {
        renderer.clear(clearColor);
        guiDocument.render(renderer);
      };
    } else if (guiDocument == null) {
      renderFunction = renderer -> {
        renderer.clear(clearColor);
        graphics.render(renderer);
      };
    } else {
      renderFunction = renderer -> {
        renderer.clear(clearColor);
        graphics.render(renderer);
        guiDocument.render(renderer);
      };
    }
  }
}
