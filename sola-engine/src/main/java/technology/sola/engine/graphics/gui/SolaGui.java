package technology.sola.engine.graphics.gui;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Consumer;
import java.util.function.Function;

public class SolaGui {
  public final GuiElementGlobalProperties globalProperties;
  private GuiElement<?> root;

  public SolaGui(AssetPoolProvider assetPoolProvider) {
    globalProperties = new GuiElementGlobalProperties(assetPoolProvider);
  }

  public <T extends GuiElement<P>, P extends GuiElementProperties> T createElement(
    Function<P, T> elementConstructor,
    Function<GuiElementGlobalProperties, P> elementPropertiesConstructor
  ) {
    return createElement(elementConstructor, elementPropertiesConstructor, p -> {});
  }

  public <T extends GuiElement<P>, P extends GuiElementProperties> T createElement(
    Function<P, T> elementConstructor,
    Function<GuiElementGlobalProperties, P> elementPropertiesConstructor,
    Consumer<P> propertiesInitializer
  ) {
    P properties = elementPropertiesConstructor.apply(globalProperties);

    propertiesInitializer.accept(properties);

    return elementConstructor.apply(properties);
  }

  public void setGuiRoot(GuiElement<?> guiElement) {
    this.root = guiElement;
  }

  public void render(Renderer renderer) {
    if (root != null) {
      root.render(renderer);
    }
  }

  public void onMousePressed(MouseEvent event) {
    if (root != null) {
      root.handleMouseEvent(event, "press");
    }
  }

  public void onMouseReleased(MouseEvent event) {
    if (root != null) {
      root.handleMouseEvent(event, "release");
    }
  }

  public void onMouseMoved(MouseEvent event) {
    if (root != null) {
      root.handleMouseEvent(event, "move");
    }
  }
}
