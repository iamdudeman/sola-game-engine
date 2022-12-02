package technology.sola.engine.core.module.graphics.gui;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.module.SolaModule;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.gui.GuiElement;
import technology.sola.engine.graphics.gui.properties.GuiElementBaseProperties;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * SolaGui is a {@link SolaModule} that handles registering event listeners and loading a default {@link Font} for
 * {@link GuiElement}s to use. It also contains several utility methods for creating instances of gui elements, setting
 * the root element of a tree, and searching for elements in the tree.
 */
@SolaModule
public class SolaGui {
  public final GuiElementGlobalProperties globalProperties;
  public final EventHub eventHub;
  private final AssetLoaderProvider assetLoaderProvider;
  private GuiElement<?> rootGuiElement;
  private GuiElement<?> focussedElement;
  private Renderer renderer;

  /**
   * Creates a new instances of {@link SolaGui}. Registers event listeners for the {@link SolaPlatform} and prepares a
   * default {@link Font} to be loaded.
   *
   * @param assetLoaderProvider the {@link AssetLoaderProvider} to load the {@code Font}
   * @param platform            the {@link SolaPlatform} instance
   * @param eventHub            the {@link EventHub} instance
   * @return this
   */
  public static SolaGui useModule(AssetLoaderProvider assetLoaderProvider, SolaPlatform platform, EventHub eventHub) {
    SolaGui solaGui = new SolaGui(assetLoaderProvider, eventHub);

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

  /**
   * Renders the root {@link GuiElement} and its children.
   */
  public void render() {
    if (rootGuiElement != null) {
      rootGuiElement.render(renderer);
    }
  }

  public <T extends GuiElement<P>, P extends GuiElementBaseProperties<?>> T createElement(
    GuiElementCreator<T, P> elementCreator
  ) {
    return elementCreator.create(this);
  }

  public <T extends GuiElement<P>, P extends GuiElementBaseProperties<?>> T createElement(
    GuiElementCreator<T, P> elementCreator,
    Consumer<P> propertiesInitializer
  ) {
    var ele = elementCreator.create(this);

    propertiesInitializer.accept(ele.properties());

    return ele;
  }

  // TODO consider a createContainer method as well that allows passing in children

  @Deprecated
  public <T extends GuiElement<P>, P extends GuiElementBaseProperties<?>> T createElement(
    GuiElementCreatorOld<T, P> elementCreator,
    Function<GuiElementGlobalProperties, P> elementPropertiesConstructor
  ) {
    return createElement(elementCreator, elementPropertiesConstructor, p -> {
    });
  }

  @Deprecated
  public <T extends GuiElement<P>, P extends GuiElementBaseProperties<?>> T createElement(
    GuiElementCreatorOld<T, P> elementCreator,
    Function<GuiElementGlobalProperties, P> elementPropertiesConstructor,
    Consumer<P> propertiesInitializer
  ) {
    P properties = elementPropertiesConstructor.apply(globalProperties);

    propertiesInitializer.accept(properties);

    return elementCreator.create(this, properties);
  }

  public void setGuiRoot(GuiElement<?> guiElement) {
    setGuiRoot(guiElement, 0, 0);
  }

  public void setGuiRoot(GuiElement<?> guiElement, int x, int y) {
    guiElement.setPosition(x, y);
    this.rootGuiElement = guiElement;
    focusElement(guiElement);
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

  public GuiElement<?> getElementById(String id) {
    return rootGuiElement.getElementById(id);
  }

  public <T extends GuiElement<?>> T getElementById(String id, Class<T> clazz) {
    GuiElement<?> guiElement = getElementById(id);

    if (guiElement == null) {
      return null;
    }

    return clazz.cast(guiElement);
  }

  private SolaGui(AssetLoaderProvider assetLoaderProvider, EventHub eventHub) {
    this.assetLoaderProvider = assetLoaderProvider;
    this.eventHub = eventHub;
    this.globalProperties = new GuiElementGlobalProperties(() -> rootGuiElement);
  }

  public interface GuiElementCreator<T extends GuiElement<P>, P extends GuiElementBaseProperties<?>> {
    T create(SolaGui solaGui);
  }

  public interface GuiElementCreatorOld<T extends GuiElement<P>, P extends GuiElementBaseProperties<?>> {
    T create(SolaGui solaGui, P properties);
  }
}
