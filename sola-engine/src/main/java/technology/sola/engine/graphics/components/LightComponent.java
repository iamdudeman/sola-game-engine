package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;

public class LightComponent implements Component {
  private float radius;
  private float intensity;
  private float offsetX;
  private float offsetY;

  public LightComponent(float radius) {
    this(radius, 1f);
  }

  public LightComponent(float radius, float intensity) {
    this.radius = radius;
    this.intensity = intensity;
  }

  public float getRadius() {
    return radius;
  }

  public float getIntensity() {
    return intensity;
  }

  public void setRadius(float radius) {
    this.radius = radius;
  }

  public void setIntensity(float intensity) {
    this.intensity = intensity;
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
