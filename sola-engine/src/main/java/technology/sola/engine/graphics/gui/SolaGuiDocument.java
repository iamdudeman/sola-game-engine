package technology.sola.engine.graphics.gui;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.event.EventHub;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.gui.properties.GuiElementBaseProperties;
import technology.sola.engine.graphics.gui.properties.GuiElementGlobalProperties;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;

/**
 * SolaGuiDocument is a container for {@link GuiElement}s. It also contains several utility methods for creating
 * instances of gui elements, setting the root element of a tree, and searching for elements in the tree.
 */
public class SolaGuiDocument {
  public final GuiElementGlobalProperties globalProperties;
  public final EventHub eventHub;
  private final AssetLoaderProvider assetLoaderProvider;
  private GuiElement<?> rootGuiElement;
  private GuiElement<?> focussedElement;

  /**
   * Creates a new instances of {@link SolaGuiDocument}. Registers event listeners for the {@link SolaPlatform} and
   * prepares a default {@link Font} to be loaded.
   *
   * @param platform            the {@link SolaPlatform} instance
   * @param assetLoaderProvider the {@link AssetLoaderProvider} to load the {@code Font}
   * @param eventHub            the {@link EventHub} instance
   */
  public SolaGuiDocument(SolaPlatform platform, AssetLoaderProvider assetLoaderProvider, EventHub eventHub) {
    this.assetLoaderProvider = assetLoaderProvider;
    this.eventHub = eventHub;
    this.globalProperties = new GuiElementGlobalProperties(() -> rootGuiElement);

    initializeForPlatform(platform);
  }

  /**
   * Renders the root {@link GuiElement} and its children.
   *
   * @param renderer the {@link Renderer} instance
   */
  public void render(Renderer renderer) {
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
    T guiElement = elementCreator.create(this);

    propertiesInitializer.accept(guiElement.properties());

    return guiElement;
  }

  public <T extends GuiElementContainer<P>, P extends GuiElementBaseProperties<?>> T createElement(
    GuiElementCreator<T, P> elementCreator,
    Consumer<P> propertiesInitializer,
    GuiElement<?> ...children
  ) {
    T guiElementContainer = createElement(elementCreator, propertiesInitializer);

    guiElementContainer.addChild(children);

    return guiElementContainer;
  }

  public void setGuiRoot(GuiElement<?> guiElement) {
    setGuiRoot(guiElement, 0, 0);
  }

  public void setGuiRoot(GuiElement<?> guiElement, int x, int y) {
    guiElement.setPosition(x, y);
    this.rootGuiElement = guiElement;
    focusElement(guiElement);
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

  private void onKeyPressed(KeyEvent keyEvent) {
    if (rootGuiElement != null) {
      rootGuiElement.handleKeyEvent(new GuiKeyEvent(keyEvent, GuiKeyEvent.Type.PRESS));
    }
  }

  private void onKeyReleased(KeyEvent keyEvent) {
    if (rootGuiElement != null) {
      rootGuiElement.handleKeyEvent(new GuiKeyEvent(keyEvent, GuiKeyEvent.Type.RELEASE));
    }
  }

  private void onMousePressed(MouseEvent event) {
    if (rootGuiElement != null) {
      rootGuiElement.handleMouseEvent(event, "press");
    }
  }

  private void onMouseReleased(MouseEvent event) {
    if (rootGuiElement != null) {
      rootGuiElement.handleMouseEvent(event, "release");
    }
  }

  private void onMouseMoved(MouseEvent event) {
    if (rootGuiElement != null) {
      rootGuiElement.handleMouseEvent(event, "move");
    }
  }

  private void initializeForPlatform(SolaPlatform platform) {
    // Register event listeners
    platform.onKeyPressed(this::onKeyPressed);
    platform.onKeyReleased(this::onKeyReleased);
    platform.onMouseMoved(this::onMouseMoved);
    platform.onMousePressed(this::onMousePressed);
    platform.onMouseReleased(this::onMouseReleased);

    // Prepare default font
    AssetLoader<Font> fontAssetLoader = assetLoaderProvider.get(Font.class);

    if (!fontAssetLoader.hasAssetMapping(GuiElementGlobalProperties.DEFAULT_FONT_ASSET_ID)) {
      fontAssetLoader.addAsset(GuiElementGlobalProperties.DEFAULT_FONT_ASSET_ID, DefaultFont.get());
    }
  }

  public interface GuiElementCreator<T extends GuiElement<P>, P extends GuiElementBaseProperties<?>> {
    T create(SolaGuiDocument solaGuiDocument);
  }
}
