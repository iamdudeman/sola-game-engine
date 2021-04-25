package technology.sola.engine.examples;

import technology.sola.engine.graphics.Color;
import technology.sola.engine.platform.JavaFxApplication;
import technology.sola.engine.platform.JavaFxContainer;

public class Main  {
  public static void main(String[] args) {
    JavaFxContainer javaFxContainer = new JavaFxContainer("Test" ,800, 600);

    javaFxContainer.getRenderer().clear();
    javaFxContainer.getRenderer().setPixel(5, 5, Color.WHITE);

    JavaFxApplication.start(javaFxContainer, args);
  }
}
