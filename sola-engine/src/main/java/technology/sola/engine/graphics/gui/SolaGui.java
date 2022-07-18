package technology.sola.engine.graphics.gui;

import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.input.MouseEvent;

import java.util.function.Function;

public class SolaGui {
  private GuiElement<?> root;
  private final AssetPoolProvider assetPoolProvider;

  public SolaGui(AssetPoolProvider assetPoolProvider) {
    this.assetPoolProvider = assetPoolProvider;
  }

  public <T extends GuiElement<P>, P extends GuiElementProperties> T createElement(Function<P, T> elementConstructor, P elementProperties) {
    return elementConstructor.apply(elementProperties);
  }

  public <T extends GuiElement<P>, P extends GuiElementProperties> T createElement(ElementConstructorWithAssets<T, P> elementConstructor, P elementProperties) {
    return elementConstructor.apply(assetPoolProvider, elementProperties);
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

  public interface ElementConstructorWithAssets<T extends GuiElement<P>, P extends GuiElementProperties> {
    T apply(AssetPoolProvider assetPoolProvider, P properties);
  }
}
