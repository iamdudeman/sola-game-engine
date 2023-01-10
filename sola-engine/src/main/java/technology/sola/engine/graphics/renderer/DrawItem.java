package technology.sola.engine.graphics.renderer;

/**
 * DrawItem hold logic to draw things onto a {@link Renderer}.
 */
public interface DrawItem {
  /**
   * Draws this DrawItem to a {@link Renderer}.
   *
   * @param renderer the {@code Renderer}
   */
  void draw(Renderer renderer);
}
