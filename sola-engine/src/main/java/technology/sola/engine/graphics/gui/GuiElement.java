package technology.sola.engine.graphics.gui;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.event.GuiKeyEvent;
import technology.sola.engine.graphics.gui.properties.GuiElementBaseProperties;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.input.MouseEvent;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.util.function.Consumer;

public abstract class GuiElement<T extends GuiElementBaseProperties<?>> {
  protected final SolaGuiDocument document;
  protected final T properties;
  private int x;
  private int y;

  private Consumer<GuiKeyEvent> onKeyPressCallback;
  private Consumer<GuiKeyEvent> onKeyReleaseCallback;
  private Consumer<MouseEvent> onMouseEnterCallback;
  private Consumer<MouseEvent> onMouseExitCallback;
  private Consumer<MouseEvent> onMouseDownCallback;
  private Consumer<MouseEvent> onMouseUpCallback;

  private boolean wasMouseOverElement = false;

  public GuiElement(SolaGuiDocument document, T properties) {
    this.document = document;
    this.properties = properties;
  }

  public abstract int getContentWidth();

  public abstract int getContentHeight();

  public int getWidth() {
    if (properties.getWidth() != null) {
      return properties.getWidth();
    }

    return getContentWidth() + getNonContentWidth();
  }

  public int getHeight() {
    if (properties.getHeight() != null) {
      return properties.getHeight();
    }

    return getContentHeight() + getNonContentHeight();
  }

  public int getNonContentWidth() {
    return properties.padding.getLeft() + properties.padding.getRight() + properties().getBorderSize() * 2;
  }

  public int getNonContentHeight() {
    return properties.padding.getTop() + properties.padding.getBottom() + properties().getBorderSize() * 2;
  }

  public abstract void recalculateLayout();

  public boolean isLayoutChanged() {
    return properties().isLayoutChanged();
  }

  public abstract void renderSelf(Renderer renderer, int x, int y);

  public void render(Renderer renderer) {
    if (isLayoutChanged()) {
      recalculateLayout();
      properties.setLayoutChanged(false);
    }

    if (!properties.isHidden()) {
      int borderOffset = properties.getBorderColor() == null ? 0 : 1;

      Color hoverBackgroundColor = properties.hover.getBackgroundColor() == null ? properties.getBackgroundColor() : properties.hover.getBackgroundColor();
      Color backgroundColor = isHovered() ? hoverBackgroundColor : properties.getBackgroundColor();

      if (backgroundColor != null) {
        renderer.setBlendMode(BlendMode.NORMAL);
        renderer.fillRect(
          x + borderOffset, y + borderOffset,
          getWidth() - borderOffset, getHeight() - borderOffset,
          backgroundColor
        );
      }

      renderSelf(renderer, x + borderOffset + properties.padding.getLeft(), y + borderOffset + properties.padding.getTop());

      Color hoverBorderColor = properties.hover.getBorderColor() == null ? properties.getBorderColor() : properties.hover.getBorderColor();
      Color borderColor = isHovered() ? hoverBorderColor : properties.getBorderColor();

      if (borderColor != null) {
        renderer.setBlendMode(BlendMode.NORMAL);
        renderer.drawRect(x, y, getWidth(), getHeight(), borderColor);
      }

      if (isFocussed() && properties.getFocusOutlineColor() != null) {
        renderer.drawRect(x - 1, y - 1, getWidth() + 2, getHeight() + 2, properties.getFocusOutlineColor());
      }
    }
  }

  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
    properties.setLayoutChanged(true);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isHovered() {
    return wasMouseOverElement;
  }

  public boolean isFocussed() {
    return document.isFocussedElement(this);
  }

  public void requestFocus() {
    document.focusElement(this);
  }

  public GuiElement<?> getElementById(String id) {
    return id.equals(properties.getId()) ? this : null;
  }

  public void onKeyPress(GuiKeyEvent keyEvent) {
    if (onKeyPressCallback != null) {
      onKeyPressCallback.accept(keyEvent);
    }
  }

  public void setOnKeyPressCallback(Consumer<GuiKeyEvent> onKeyPressCallback) {
    this.onKeyPressCallback = onKeyPressCallback;
  }

  public void onKeyRelease(GuiKeyEvent keyEvent) {
    if (onKeyReleaseCallback != null) {
      onKeyReleaseCallback.accept(keyEvent);
    }
  }

  public void setOnKeyReleaseCallback(Consumer<GuiKeyEvent> onKeyReleaseCallback) {
    this.onKeyReleaseCallback = onKeyReleaseCallback;
  }

  public void onMouseEnter(MouseEvent mouseEvent) {
    if (!wasMouseOverElement) {
      wasMouseOverElement = true;

      if (onMouseEnterCallback != null) {
        onMouseEnterCallback.accept(mouseEvent);
      }
    }
  }

  public void setOnMouseEnterCallback(Consumer<MouseEvent> callback) {
    onMouseEnterCallback = callback;
  }

  public void onMouseExit(MouseEvent mouseEvent) {
    if (wasMouseOverElement) {
      wasMouseOverElement = false;

      if (onMouseExitCallback != null) {
        onMouseExitCallback.accept(mouseEvent);
      }
    }
  }

  public void setOnMouseExitCallback(Consumer<MouseEvent> callback) {
    onMouseExitCallback = callback;
  }

  public void onMouseDown(MouseEvent mouseEvent) {
    if (onMouseDownCallback != null) {
      onMouseDownCallback.accept(mouseEvent);
    }
  }

  public void setOnMouseDownCallback(Consumer<MouseEvent> callback) {
    onMouseDownCallback = callback;
  }

  public void onMouseUp(MouseEvent mouseEvent) {
    if (onMouseUpCallback != null) {
      onMouseUpCallback.accept(mouseEvent);
    }
  }

  public void setOnMouseUpCallback(Consumer<MouseEvent> callback) {
    onMouseUpCallback = callback;
  }

  public T properties() {
    return properties;
  }

  public void handleKeyEvent(GuiKeyEvent event) {
    if (!isFocussed()) {
      return;
    }

    switch (event.getType()) {
      case PRESS -> onKeyPress(event);
      case RELEASE -> onKeyRelease(event);
    }
  }

  public void handleMouseEvent(MouseEvent event, String eventType) {
    T properties = properties();

    if (properties.isHidden()) {
      return;
    }

    int width = getWidth();
    int height = getHeight();

    Rectangle guiElementBounds = new Rectangle(new Vector2D(x, y), new Vector2D(x + width, y + height));

    if (guiElementBounds.contains(new Vector2D(event.x(), event.y()))) {
      switch (eventType) {
        case "press" -> onMouseDown(event);
        case "release" -> onMouseUp(event);
        case "move" -> onMouseEnter(event);
      }
    } else {
      onMouseExit(event);
    }
  }
}
