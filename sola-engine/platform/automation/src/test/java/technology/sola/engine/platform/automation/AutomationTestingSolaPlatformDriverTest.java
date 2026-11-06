package technology.sola.engine.platform.automation;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.logging.JavaSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

@NullMarked
class AutomationTestingSolaPlatformDriverTest {
  static {
    SolaLogger.configure(SolaLogLevel.WARNING, new JavaSolaLoggerFactory());
  }

  @Test
  void test() {
    AutomationTestingSolaPlatform automationTestingSolaPlatform = new AutomationTestingSolaPlatform(
      new AutomationTestingSolaPlatformConfig()
    );

    automationTestingSolaPlatform.play(new TestSola());


    var driver = automationTestingSolaPlatform.getDriver();


    driver.advanceTime(10);

    driver.snapshot();
  }

  private static class TestSola extends Sola {
    protected TestSola() {
      super(new SolaConfiguration("Test", 800, 600, 30));
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onRender(Renderer renderer) {
      renderer.drawCircle(20, 20, 10, Color.WHITE);
    }
  }
}
