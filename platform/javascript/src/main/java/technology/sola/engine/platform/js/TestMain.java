package technology.sola.engine.platform.js;

import technology.sola.engine.core.AbstractSola;
import technology.sola.engine.graphics.Color;

public class TestMain {
  public static void main(String[] args) {
    var sola = new TestGame();
    var solaPlatform = new JavaScriptSolaPlatform();

    solaPlatform.launch(sola);
  }

  private static class TestGame extends AbstractSola {
    public TestGame() {
      config(600, 400, 30, false);
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onRender() {
      renderer.clear();
      renderer.fillRect(50, 50, 100, 100, Color.BLUE);
    }
  }
}
