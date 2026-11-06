package technology.sola.engine.platform.automation;

import technology.sola.engine.core.SolaPlatformConfig;
import technology.sola.engine.graphics.Color;

public record AutomationTestingSolaPlatformConfig(
  Color backgroundColor
) implements SolaPlatformConfig {
  public AutomationTestingSolaPlatformConfig() {
    this(Color.BLACK);
  }

  @Override
  public boolean useSoftwareRendering() {
    return true;
  }
}
