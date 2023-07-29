package technology.sola.engine.graphics.components;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;

public class LightComponent implements Component {
  private Color lightColor;
  private float radius;
  private float intensity;
}

// todo light types
//   Point (starts in a center and goes all around
//   Spot angle + range
