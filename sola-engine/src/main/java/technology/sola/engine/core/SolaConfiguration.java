package technology.sola.engine.core;

import org.jspecify.annotations.NullMarked;

/**
 * Creates a {@link SolaConfiguration}.
 *
 * @param title                  the title for the {@link Sola}.
 * @param rendererWidth          the width of the {@link technology.sola.engine.graphics.renderer.Renderer}
 * @param rendererHeight         the height of the {@link technology.sola.engine.graphics.renderer.Renderer}
 * @param targetUpdatesPerSecond the target updates per second for the game loop
 */
@NullMarked
public record SolaConfiguration(
  String title,
  int rendererWidth,
  int rendererHeight,
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
}
