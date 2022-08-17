package technology.sola.engine.core.module.graphics.gui;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.module.SolaModule;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.GuiElementProperties;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;
import java.util.function.Function;

@SolaModule
public class SolaGui {
  public final GuiElementGlobalProperties globalProperties;
  private final AssetLoaderProvider assetLoaderProvider;
  private GuiElement<?> rootGuiElement;
  private GuiElement<?> focussedElement;
  private Renderer renderer;

  public static SolaGui createInstance(AssetLoaderProvider assetLoaderProvider, SolaPlatform platform) {
    SolaGui solaGui = new SolaGui(assetLoaderProvider);

    solaGui.renderer = platform.getRenderer();
    platform.onKeyPressed(solaGui::onKeyPressed);
    platform.onKeyReleased(solaGui::onKeyReleased);
    platform.onMouseMoved(solaGui::onMouseMoved);
    platform.onMousePressed(solaGui::onMousePressed);
    platform.onMouseReleased(solaGui::onMouseReleased);

    AssetLoader<Font> fontAssetLoader = assetLoaderProvider.get(Font.class);

    if (!fontAssetLoader.hasAssetMapping(GuiElementGlobalProperties.DEFAULT_FONT_ASSET_ID)) {
      fontAssetLoader.addAsset(GuiElementGlobalProperties.DEFAULT_FONT_ASSET_ID, DefaultFont.get());
    }

    return solaGui;
  }

  public <T extends GuiElement<P>, P extends GuiElementProperties> T createElement(
    GuiElementCreator<T, P> elementCreator,
    Function<GuiElementGlobalProperties, P> elementPropertiesConstructor
  ) {
    return createElement(elementCreator, elementPropertiesConstructor, p -> {});
  }

  public <T extends GuiElement<P>, P extends GuiElementProperties> T createElement(
    GuiElementCreator<T, P> elementCreator,
    Function<GuiElementGlobalProperties, P> elementPropertiesConstructor,
    Consumer<P> propertiesInitializer
  ) {
    P properties = elementPropertiesConstructor.apply(globalProperties);

    propertiesInitializer.accept(properties);

    return elementCreator.create(this, properties);
  }

  public void setGuiRoot(GuiElement<?> guiElement) {
    this.rootGuiElement = guiElement;
    this.focusElement(guiElement);
  }

  public void render() {
    if (rootGuiElement != null) {
      rootGuiElement.render(renderer);
    }
  }

  public void onKeyPressed(KeyEvent keyEvent) {
    if (rootGuiElement != null) {
      rootGuiElement.handleKeyEvent(new GuiKeyEvent(keyEvent, GuiKeyEvent.Type.PRESS));
    }
  }

  public void onKeyReleased(KeyEvent keyEvent) {
    if (rootGuiElement != null) {
      rootGuiElement.handleKeyEvent(new GuiKeyEvent(keyEvent, GuiKeyEvent.Type.RELEASE));
    }
  }

  public void onMousePressed(MouseEvent event) {
    if (rootGuiElement != null) {
      rootGuiElement.handleMouseEvent(event, "press");
    }
  }

  public void onMouseReleased(MouseEvent event) {
    if (rootGuiElement != null) {
      rootGuiElement.handleMouseEvent(event, "release");
    }
  }

  public void onMouseMoved(MouseEvent event) {
    if (rootGuiElement != null) {
      rootGuiElement.handleMouseEvent(event, "move");
    }
  }

  public AssetLoaderProvider getAssetLoaderProvider() {
    return assetLoaderProvider;
  }

  public boolean isFocussedElement(GuiElement<?> guiElement) {
    return this.focussedElement == guiElement;
  }

  public void focusElement(GuiElement<?> guiElement) {
    if (guiElement.properties().isFocusable()) {
      this.focussedElement = guiElement;
    }
  }

  private SolaGui(AssetLoaderProvider assetLoaderProvider) {
    this.assetLoaderProvider = assetLoaderProvider;
    this.globalProperties = new GuiElementGlobalProperties(() -> rootGuiElement);
  }

  public interface GuiElementCreator<T extends GuiElement<P>, P extends GuiElementProperties> {
    T create(SolaGui solaGui, P properties);
  }
}
