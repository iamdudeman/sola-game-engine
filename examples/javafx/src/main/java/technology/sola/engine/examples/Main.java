package technology.sola.engine.examples;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.platform.JavaFxApplication;
import technology.sola.engine.platform.JavaFxContainer;

public class Main  {
  public static void main(String[] args) {
    JavaFxContainer javaFxContainer = new JavaFxContainer("Test" ,800, 600);

    Renderer renderer = javaFxContainer.getRenderer();

    renderer.clear();
    renderer.setPixel(5, 5, Color.WHITE);
    renderer.setPixel(6, 5, Color.BLUE);
    renderer.setPixel(6, 6, Color.RED);
    renderer.setPixel(6, 7, Color.GREEN);

    renderer.drawLine(20, 50, 20, 100, Color.WHITE);
    renderer.drawLine(50, 20, 100, 20, Color.WHITE);

    renderer.drawRect(100, 100, 60, 80, Color.GREEN);

    JavaFxApplication.start(javaFxContainer, args);
  }
}
