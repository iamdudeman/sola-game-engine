package technology.sola.engine.graphics.gui;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;
import java.util.function.Function;

public class SolaGui {
  public final GuiElementGlobalProperties globalProperties;
  private GuiElement<?> rootGuiElement;
  private GuiElement<?> focussedElement;

  public SolaGui(AssetPoolProvider assetPoolProvider) {
    globalProperties = new GuiElementGlobalProperties(assetPoolProvider);
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

  public boolean isFocussedElement(GuiElement<?> guiElement) {
    return this.focussedElement == guiElement;
  }

  public void focusElement(GuiElement<?> guiElement) {
    this.focussedElement = guiElement;
  }

  public void render(Renderer renderer) {
    if (rootGuiElement != null) {
      rootGuiElement.render(renderer);
    }
  }

  public void onKeyPressed(KeyEvent keyEvent) {
    if (rootGuiElement != null) {
      rootGuiElement.handleKeyEvent(keyEvent, "press");
    }
  }

  public void onKeyReleased(KeyEvent keyEvent) {
    if (rootGuiElement != null) {
      rootGuiElement.handleKeyEvent(keyEvent, "release");
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

  public interface GuiElementCreator<T extends GuiElement<P>, P extends GuiElementProperties> {
    T create(SolaGui solaGui, P properties);
  }

  public record GuiElementQueryResult(GuiElementContainer<?> parent, GuiElement<?> element) {
  }
}
