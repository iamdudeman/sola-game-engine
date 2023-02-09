package technology.sola.engine.defaults;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.graphics.modules.CircleGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.DebugGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.RectangleGraphicsModule;
import technology.sola.engine.defaults.graphics.modules.SpriteGraphicsModule;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.SolaGuiDocument;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.system.CollisionDetectionSystem;

import java.util.function.Consumer;

public abstract class SolaWithDefaults extends Sola {
  protected SolaGraphics solaGraphics;
  protected SolaPhysics solaPhysics;
  protected SolaGuiDocument solaGuiDocument;
  private Consumer<Renderer> renderFunction = renderer -> {
  };

  protected SolaWithDefaults(SolaConfiguration.Builder solaConfigurationBuilder) {
    super(solaConfigurationBuilder);
  }

  protected SolaWithDefaults(SolaConfiguration configuration) {
    super(configuration);
  }

  protected abstract void onInit(DefaultsConfigurator defaultsConfigurator);

  @Override
  protected void onInit() {
    onInit(new DefaultsConfigurator());
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderFunction.accept(renderer);
  }

  public class DefaultsConfigurator {
    private boolean isDebug = false;
    private Color clearColor = Color.BLACK;

    public DefaultsConfigurator useDebug() {
      if (!isDebug) {
        this.isDebug = true;

        if (solaGraphics != null && solaPhysics != null) {
          solaGraphics.getGraphicsModuleList().add(new DebugGraphicsModule(solaEcs.getSystem(CollisionDetectionSystem.class)));
        }
      }

      return this;
    }

    public DefaultsConfigurator useClearColor(Color clearColor) {
      this.clearColor = clearColor;
      rebuildRenderFunction();

      return this;
    }

    public DefaultsConfigurator usePhysics() {
      if (solaPhysics == null) {
        solaPhysics = new SolaPhysics(eventHub);

        solaEcs.addSystems(solaPhysics.getSystems());

        if (isDebug && solaGraphics != null) {
          solaGraphics.getGraphicsModuleList().add(new DebugGraphicsModule(solaEcs.getSystem(CollisionDetectionSystem.class)));
        }
      }

      return this;
    }

    public DefaultsConfigurator useGraphics() {
      if (solaGraphics == null) {
        solaGraphics = new SolaGraphics(solaEcs);

        solaEcs.addSystems(solaGraphics.getSystems());

        AssetLoader<SpriteSheet> spriteSheetAssetLoader = assetLoaderProvider.get(SpriteSheet.class);

        solaGraphics.getGraphicsModuleList().add(new CircleGraphicsModule());
        solaGraphics.getGraphicsModuleList().add(new RectangleGraphicsModule());
        solaGraphics.getGraphicsModuleList().add(new SpriteGraphicsModule(spriteSheetAssetLoader));

        if (isDebug && solaPhysics != null) {
          solaGraphics.getGraphicsModuleList().add(new DebugGraphicsModule(solaEcs.getSystem(CollisionDetectionSystem.class)));
        }

        rebuildRenderFunction();
      }

      return this;
    }

    public DefaultsConfigurator useGui() {
      if (solaGuiDocument == null) {
        solaGuiDocument = new SolaGuiDocument(platform, assetLoaderProvider, eventHub);
        rebuildRenderFunction();
      }

      return this;
    }

    private void rebuildRenderFunction() {
      if (solaGraphics != null && solaGuiDocument != null) {
        renderFunction = renderer -> {
          renderer.clear(clearColor);
          solaGraphics.render(renderer);
          solaGuiDocument.render(renderer);
        };
      } else if (solaGraphics != null) {
        renderFunction = renderer -> {
          renderer.clear(clearColor);
          solaGraphics.render(renderer);
        };
      } else if (solaGuiDocument != null) {
        renderFunction = renderer -> {
          renderer.clear(clearColor);
          solaGuiDocument.render(renderer);
        };
      }
    }
  }
}
