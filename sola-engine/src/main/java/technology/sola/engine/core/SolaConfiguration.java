package technology.sola.engine.core;

/**
 * Creates a {@link SolaConfiguration}.
 *
 * @param title                  the title for the {@link Sola}.
 * @param rendererWidth          the width of the {@link technology.sola.engine.graphics.renderer.Renderer}
 * @param rendererHeight         the height of the {@link technology.sola.engine.graphics.renderer.Renderer}
 * @param targetUpdatesPerSecond the target updates per second for the game loop
 */
public record SolaConfiguration(
  String title,
  int rendererWidth, int rendererHeight,
  int targetUpdatesPerSecond
) {
  /**
   * Creates a {@link SolaConfiguration} with targetUpdatePerSecond defaulted to 60.
   *
   * @param title          the title for the {@link Sola}.
   * @param rendererWidth  the width of the {@link technology.sola.engine.graphics.renderer.Renderer}
   * @param rendererHeight the height of the {@link technology.sola.engine.graphics.renderer.Renderer}
   */
  public SolaConfiguration(String title, int rendererWidth, int rendererHeight) {
    this(title, rendererWidth, rendererHeight, 60);
  }

  /**
   * Creates a {@link SolaConfiguration} {@link Builder} with targetUpdatePerSecond defaulted to 60.
   *
   * @param title          the title for the {@link Sola}.
   * @param rendererWidth  the width of the {@link technology.sola.engine.graphics.renderer.Renderer}
   * @param rendererHeight the height of the {@link technology.sola.engine.graphics.renderer.Renderer}
   */
  public static Builder build(String title, int rendererWidth, int rendererHeight) {
    return new Builder(title, rendererWidth, rendererHeight);
  }

  /**
   * Builder for {@link SolaConfiguration}.
   */
  public static class Builder {
    private final String title;
    private final int rendererWidth;
    private final int rendererHeight;
    private int targetUpdatesPerSecond = 60;

    private Builder(String title, int rendererWidth, int rendererHeight) {
      this.title = title;
      this.rendererWidth = rendererWidth;
      this.rendererHeight = rendererHeight;
    }

    /**
     * Sets the target updates per second to use for {@link SolaConfiguration} instances created from this builder.
     *
     * @param targetUpdatesPerSecond the target updates per second for the game loop
     * @return this
     */
    public Builder withTargetUpdatesPerSecond(int targetUpdatesPerSecond) {
      this.targetUpdatesPerSecond = targetUpdatesPerSecond;
      return this;
    }

    /**
     * Builds a {@link SolaConfiguration} from builder settings.
     *
     * @return a SolaConfiguration instance
     */
    public SolaConfiguration build() {
      return new SolaConfiguration(title, rendererWidth, rendererHeight, targetUpdatesPerSecond);
    }
  }
}
