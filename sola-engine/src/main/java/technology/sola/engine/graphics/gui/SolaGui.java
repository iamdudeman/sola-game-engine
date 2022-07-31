package technology.sola.engine.graphics.gui;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.annotation.SolaUseConfiguration;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;
import java.util.function.Function;

@SolaUseConfiguration
public class SolaGui {
  public final GuiElementGlobalProperties globalProperties = new GuiElementGlobalProperties();
  private final AssetPoolProvider assetPoolProvider;
  private GuiElement<?> rootGuiElement;
  private GuiElement<?> focussedElement;

  public static SolaGui use(AssetPoolProvider assetPoolProvider, SolaPlatform platform) {
    SolaGui solaGui = new SolaGui(assetPoolProvider);

    platform.onKeyPressed(solaGui::onKeyPressed);
    platform.onKeyReleased(solaGui::onKeyReleased);
    platform.onMouseMoved(solaGui::onMouseMoved);
    platform.onMousePressed(solaGui::onMousePressed);
    platform.onMouseReleased(solaGui::onMouseReleased);

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

  public void render(Renderer renderer) {
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

  public AssetPoolProvider getAssetPoolProvider() {
    return assetPoolProvider;
  }

  boolean isFocussedElement(GuiElement<?> guiElement) {
    return this.focussedElement == guiElement;
  }

  void focusElement(GuiElement<?> guiElement) {
    if (guiElement.properties().isFocusable()) {
      this.focussedElement = guiElement;
    }
  }

  private SolaGui(AssetPoolProvider assetPoolProvider) {
    this.assetPoolProvider = assetPoolProvider;
  }

  public interface GuiElementCreator<T extends GuiElement<P>, P extends GuiElementProperties> {
    T create(SolaGui solaGui, P properties);
  }
}
