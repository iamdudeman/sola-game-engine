package technology.sola.engine.core;

public record SolaConfiguration(
  String title,
  int rendererWidth, int rendererHeight,
  int targetUpdatesPerSecond, boolean isGameLoopRestingOn) {

  /**
   * Creates a {@link SolaConfiguration} with targetUpdatePerSecond defaulted to 60 and game loop resting off.
   *
   * @param title          the title for the {@link Sola}.
   * @param rendererWidth  the width of the {@link technology.sola.engine.graphics.renderer.Renderer}
   * @param rendererHeight the height of the {@link technology.sola.engine.graphics.renderer.Renderer}
   */
  public SolaConfiguration(String title, int rendererWidth, int rendererHeight) {
    this(title, rendererWidth, rendererHeight, 60, false);
  }

  /**
   * Creates a {@link SolaConfiguration} {@link Builder} with targetUpdatePerSecond defaulted to 60 and game loop resting off.
   *
   * @param title          the title for the {@link Sola}.
   * @param rendererWidth  the width of the {@link technology.sola.engine.graphics.renderer.Renderer}
   * @param rendererHeight the height of the {@link technology.sola.engine.graphics.renderer.Renderer}
   */
  public static Builder build(String title, int rendererWidth, int rendererHeight) {
    return new Builder(title, rendererWidth, rendererHeight);
  }

  public static class Builder {
    private final String title;
    private final int rendererWidth;
    private final int rendererHeight;
    private int targetUpdatesPerSecond = 60;
    private boolean isGameLoopRestingOn = false;

    private Builder(String title, int rendererWidth, int rendererHeight) {
      this.title = title;
      this.rendererWidth = rendererWidth;
      this.rendererHeight = rendererHeight;
    }

    public Builder withTargetUpdatesPerSecond(int targetUpdatesPerSecond) {
      this.targetUpdatesPerSecond = targetUpdatesPerSecond;
      return this;
    }

    public Builder withGameLoopRestingOn() {
      isGameLoopRestingOn = true;
      return this;
    }

    public SolaConfiguration build() {
      return new SolaConfiguration(title, rendererWidth, rendererHeight, targetUpdatesPerSecond, isGameLoopRestingOn);
    }
  }
}
