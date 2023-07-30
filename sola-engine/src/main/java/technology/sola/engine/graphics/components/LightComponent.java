package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;

public class LightComponent implements Component {
  private float radius;
  private Color color;
  private float offsetX;
  private float offsetY;

  public LightComponent(float radius) {
    this(radius, Color.WHITE);
  }

  public LightComponent(float radius, Color color) {
    this.radius = radius;
    this.color = color;
  }

  public float getRadius() {
    return radius;
  }

  public Color getColor() {
    return color;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public LightComponent setOffset(float x, float y) {
    this.offsetX = x;
    this.offsetY = y;

    return this;
  }

  public float getOffsetX() {
    return offsetX;
  }

  public float getOffsetY() {
    return offsetY;
  }
}
